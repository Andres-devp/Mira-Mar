package com.example.demo.e2e;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestCase2_LoginOperadorYGestionReserva extends BaseTest {

    @Test
    public void caso2_LoginOperadorGestionReservaYPago() throws InterruptedException {

        // STEP 1: Usuario cliente hace login
        driver.get(BASE_URL + "/login");
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usuario"))).sendKeys("cliente");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("123");
        ((JavascriptExecutor) driver).executeScript("document.querySelector('.auth-submit').click();");
        Thread.sleep(3000);
        assertThat(driver.getCurrentUrl().toLowerCase()).doesNotContain("login");
        System.out.println("✓ Usuario cliente logueado: " + driver.getCurrentUrl());

        // STEP 2: Usuario ve sus reservas pendientes
        driver.get(BASE_URL + "/reservations");
        Thread.sleep(2000);
        String reservaId = "";
        for (WebElement row : driver.findElements(By.cssSelector("table tbody tr"))) {
            if (row.getText().contains("PENDING")) {
                reservaId = row.findElement(By.cssSelector("td:first-child")).getText();
                System.out.println("✓ Reserva PENDING encontrada con ID: " + reservaId);
                break;
            }
        }
        assertThat(reservaId).isNotEmpty();

        // STEP 3: Abrir nueva pestaña para operador
        ((JavascriptExecutor) driver).executeScript("window.open();");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        // STEP 4: Operador hace login
        driver.get(BASE_URL + "/login");
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usuario"))).sendKeys("operador");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("123");
        ((JavascriptExecutor) driver).executeScript("document.querySelector('.auth-submit').click();");
        Thread.sleep(3000);
        assertThat(driver.getCurrentUrl().toLowerCase()).doesNotContain("login");
        System.out.println("✓ Operador logueado: " + driver.getCurrentUrl());

        // STEP 5: Operador va al detalle de la reserva
        driver.get(BASE_URL + "/reservations/" + reservaId);
        Thread.sleep(2000);
        System.out.println("✓ Operador en reserva: " + driver.getCurrentUrl());

        // STEP 6: Operador activa la reserva (checkin)
        WebElement activarBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Activar')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", activarBtn);
        Thread.sleep(2000);
        String estadoActual = driver.findElement(By.tagName("body")).getText();
        assertThat(estadoActual).contains("ACTIVE");
        System.out.println("✓ Reserva activada (checkin realizado)");

        // STEP 7: Agregar primer servicio
        WebElement agregarBtn1 = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Agregar servicio')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", agregarBtn1);
        Thread.sleep(1500);

        // Modal abierto - click en primer "Agregar" del modal
        WebElement primerAgregarModal = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector(".modal-service-card .btn--primary")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", primerAgregarModal);
        Thread.sleep(2000);
        System.out.println("✓ Primer servicio agregado");

        // STEP 8: Agregar segundo servicio
        WebElement agregarBtn2 = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Agregar servicio')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", agregarBtn2);
        Thread.sleep(1500);

        // Click en segundo servicio del modal
        java.util.List<WebElement> botonesModal = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.cssSelector(".modal-service-card .btn--primary")));
        assertThat(botonesModal.size()).isGreaterThanOrEqualTo(2);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", botonesModal.get(1));
        Thread.sleep(2000);
        System.out.println("✓ Segundo servicio agregado");

// STEP 9: Verificar total - buscar en el body
Thread.sleep(2000);
String bodyText = driver.findElement(By.tagName("body")).getText();
assertThat(bodyText).contains("Total");
System.out.println("✓ Total verificado en página");
// Imprimir para ver el valor
for (String line : bodyText.split("\n")) {
    if (line.contains("Total") || line.contains("COP")) {
        System.out.println("  → " + line.trim());
    }
}

        // STEP 10: Pagar
        WebElement pagarBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Pagar')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", pagarBtn);
        Thread.sleep(2000);
        System.out.println("✓ Pago realizado");

        // STEP 11: Verificar cuenta CLOSED
        String bodyPago = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyPago).contains("CLOSED");
        System.out.println("✓ Cuenta cerrada correctamente");

        // STEP 12: Inactivar reserva (checkout)
        WebElement inactivarBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[contains(text(),'Inactivar')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", inactivarBtn);
        Thread.sleep(2000);
        System.out.println("✓ Checkout realizado");

        // STEP 13: Verificar estado final
        String bodyFinal = driver.findElement(By.tagName("body")).getText();
        assertThat(bodyFinal).contains("INACTIVE");
        System.out.println("✓ Reserva finalizada con estado INACTIVE");

        // STEP 14: Volver a pestaña del usuario y verificar
        driver.switchTo().window(tabs.get(0));
        driver.navigate().refresh();
        Thread.sleep(2000);
        System.out.println("✓ Usuario verifica estado final");

        System.out.println("\n✓ CASO 2 COMPLETADO");
    }
}