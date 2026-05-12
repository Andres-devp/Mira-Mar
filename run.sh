#!/bin/bash

# ==============================================================================
# Script de Ejecución Automática para Mira-Mar
# ==============================================================================

DO_PULL=false
DO_FORCE_PULL=false
DO_STATUS=false

echo "================================================="
echo "🚀 Iniciando el script de ejecución de Mira-Mar"
echo "================================================="

# Parsear los parámetros de entrada
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --pull) DO_PULL=true ;;
        --force-pull) DO_FORCE_PULL=true ;;
        --status) DO_STATUS=true ;;
        -h|--help) 
            echo "Uso: ./run.sh [OPCIONES]"
            echo "Opciones:"
            echo "  --pull               Ejecuta 'git pull' antes de iniciar"
            echo "  --force-pull         Ejecuta 'git checkout .' y 'git pull' para forzar actualización"
            echo "  --status             Muestra 'git status' antes de iniciar"
            exit 0
            ;;
        *) echo "⚠️ Parámetro desconocido: $1. Usa --help para ver las opciones."; exit 1 ;;
    esac
    shift
done

# Operaciones Git
if [ "$DO_FORCE_PULL" = true ]; then
    echo "⚠️ Borrando cambios locales: git checkout . ..."
    git checkout .
    echo "⬇️ Ejecutando: git pull..."
    git pull
    echo "-------------------------------------------------"
elif [ "$DO_PULL" = true ]; then
    echo "⬇️ Ejecutando: git pull..."
    git pull
    echo "-------------------------------------------------"
fi

if [ "$DO_STATUS" = true ]; then
    echo "📌 Ejecutando: git status..."
    git status
    echo "-------------------------------------------------"
fi

# Matar procesos en puertos 8080 y 4200
echo "🧹 Limpiando puertos 8080 (Backend) y 4200 (Frontend)..."

kill_port() {
    PORT=$1
    # Buscar el PID usando lsof
    PID=$(lsof -t -i:$PORT 2>/dev/null)
    
    if [ -n "$PID" ]; then
        # Puede haber varios PIDs si hay procesos hijos, los matamos iterando o kill maneja varios
        echo "   🛑 Matando procesos en el puerto $PORT (PIDs: $PID)..."
        kill -9 $PID 2>/dev/null || echo "   ⚠️ No se pudo matar el proceso en el puerto $PORT."
    else
        # Fallback usando fuser si lsof no encuentra nada
        if command -v fuser >/dev/null 2>&1; then
            if fuser $PORT/tcp >/dev/null 2>&1; then
                echo "   🛑 Matando procesos usando fuser en el puerto $PORT..."
                fuser -k -9 $PORT/tcp >/dev/null 2>&1
            else
                echo "   ✅ El puerto $PORT está libre."
            fi
        else
            echo "   ✅ El puerto $PORT parece estar libre."
        fi
    fi
}

kill_port 8080
kill_port 4200
echo "-------------------------------------------------"

# Iniciar Backend (Spring Boot)
echo "⚙️ Iniciando el Backend (Spring Boot)..."
if [ -d "SprintBoot Web/demo" ]; then
    cd "SprintBoot Web/demo" || exit 1
    # Asegurarse de que mvnw sea ejecutable
    chmod +x mvnw
    
    echo "   📦 Construyendo e iniciando con Maven..."
    ./mvnw spring-boot:run > backend.log 2>&1 &
    BACKEND_PID=$!
    
    echo "   ✅ Backend iniciado en segundo plano (PID: $BACKEND_PID)."
    echo "   📄 Log del backend guardado en: SprintBoot Web/demo/backend.log"
    cd ../..
else
    echo "❌ Error: No se encontró la carpeta del Backend (SprintBoot Web/demo)."
    exit 1
fi
echo "-------------------------------------------------"

# Iniciar Frontend (Angular)
echo "🎨 Iniciando el Frontend (Angular)..."
if [ -d "Angular/MiraMar" ]; then
    cd "Angular/MiraMar" || exit 1
    
    # Comprobar si node_modules existe, si no, instalar
    if [ ! -d "node_modules" ]; then
        echo "   📦 Instalando dependencias de Node (npm install)... esto puede tardar un poco."
        npm install > npm_install.log 2>&1
    fi

    echo "   🚀 Ejecutando npm start..."
    npm start > frontend.log 2>&1 &
    FRONTEND_PID=$!
    
    echo "   ✅ Frontend iniciado en segundo plano (PID: $FRONTEND_PID)."
    echo "   📄 Log del frontend guardado en: Angular/MiraMar/frontend.log"
    cd ../..
else
    echo "❌ Error: No se encontró la carpeta del Frontend (Angular/MiraMar)."
    exit 1
fi
echo "-------------------------------------------------"

echo "🎉 ¡Proyecto Mira-Mar iniciándose!"
echo "👉 Backend se estará ejecutando en: http://localhost:8080"
echo "👉 Frontend se estará ejecutando en: http://localhost:4200"
echo ""
echo "🛑 Para detener los servidores:"
echo "   Puedes matar los PIDs directamente: kill -9 $BACKEND_PID $FRONTEND_PID"
echo "   O simplemente volver a ejecutar este script para que mate los puertos e inicie todo limpio."
echo "================================================="
