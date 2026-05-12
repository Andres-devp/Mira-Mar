package com.example.demo.e2e;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestCase1_RegistroYReservas extends BaseTest {

    @Test
    public void caso1_RegistroConEmailInvalido_LuegoRegistroCorrectoYDosReservas() throws InterruptedException {

        //1: Ir a landing y navegar al registro
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/login']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Crear una cuenta"))).click();

        //2: Registro con email inválido
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre"))).sendKeys("Juan Pérez");
        driver.findElement(By.id("usuario")).sendKeys("juanperez");
        ((JavascriptExecutor) driver).executeScript(
            "var el = document.getElementById('email');" +
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(el, 'email_invalido');" +
            "el.dispatchEvent(new Event('input', {bubbles:true}));");
        driver.findElement(By.id("contrasena")).sendKeys("Password123");
        driver.findElement(By.id("contrasenaConfirm")).sendKeys("Password123");
        ((JavascriptExecutor) driver).executeScript("document.getElementById('terms').click();");
        ((JavascriptExecutor) driver).executeScript("document.querySelector('.auth-submit').click();");

        //3: Verificar error de email inválido
        Thread.sleep(1500);
        assertThat(wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector(".auth-error"))).getText()).isNotEmpty();
        System.out.println("Error de email inválido verificado");

        //4: Registro correcto
        driver.navigate().refresh();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombre"))).sendKeys("Juan Pérez");
        driver.findElement(By.id("usuario")).sendKeys("juanperez2");
        driver.findElement(By.id("email")).sendKeys("juan2@example.com");
        driver.findElement(By.id("contrasena")).sendKeys("Password123");
        driver.findElement(By.id("contrasenaConfirm")).sendKeys("Password123");
        ((JavascriptExecutor) driver).executeScript("document.getElementById('terms').click();");
        ((JavascriptExecutor) driver).executeScript("document.querySelector('.auth-submit').click();");
        wait.until(ExpectedConditions.urlContains("/login"));
        System.out.println("Registro exitoso");

        //5: Login
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("usuario"))).sendKeys("juanperez2");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("Password123");
        ((JavascriptExecutor) driver).executeScript("document.querySelector('.auth-submit').click();");
        Thread.sleep(3000);
        assertThat(driver.getCurrentUrl().toLowerCase()).doesNotContain("login");
        System.out.println(" Login exitoso: " + driver.getCurrentUrl());

        //6: Primera reserva
        driver.get(BASE_URL + "/reservations/add");
        Thread.sleep(2000);

        LocalDate checkIn1 = LocalDate.now().plusDays(7);
        LocalDate checkOut1 = checkIn1.plusDays(3);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Select roomSelect1 = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("roomTypeId"))));
        Thread.sleep(1500);
        roomSelect1.selectByIndex(roomSelect1.getOptions().size() - 1);

        ((JavascriptExecutor) driver).executeScript(
            "var el = document.getElementById('fechaInicio');" +
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(el, '" + checkIn1.format(fmt) + "');" +
            "el.dispatchEvent(new Event('input', {bubbles:true}));");

        ((JavascriptExecutor) driver).executeScript(
            "var el = document.getElementById('fechaFin');" +
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(el, '" + checkOut1.format(fmt) + "');" +
            "el.dispatchEvent(new Event('input', {bubbles:true}));");

        org.openqa.selenium.WebElement cantidad1 = driver.findElement(By.id("cantidadPersonas"));
        cantidad1.clear();
        cantidad1.sendKeys("2");

        ((JavascriptExecutor) driver).executeScript(
            "var btn = document.querySelector('button.btn--primary');" +
            "btn.scrollIntoView({block:'center'}); btn.click();");
        Thread.sleep(3000);
        System.out.println("URL después de reserva 1: " + driver.getCurrentUrl());
        System.out.println("Contenido: " + driver.findElement(By.tagName("body")).getText().substring(0, 300));

        //7: Segunda reserva con fechas solapadas
        driver.get(BASE_URL + "/reservations/add");
        Thread.sleep(2000);

        LocalDate checkIn2 = checkIn1.plusDays(1);
        LocalDate checkOut2 = checkOut1.plusDays(2);

        Select roomSelect2 = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("roomTypeId"))));
        Thread.sleep(1500);
        roomSelect2.selectByIndex(roomSelect2.getOptions().size() - 1);

        ((JavascriptExecutor) driver).executeScript(
            "var el = document.getElementById('fechaInicio');" +
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(el, '" + checkIn2.format(fmt) + "');" +
            "el.dispatchEvent(new Event('input', {bubbles:true}));");

        ((JavascriptExecutor) driver).executeScript(
            "var el = document.getElementById('fechaFin');" +
            "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "setter.call(el, '" + checkOut2.format(fmt) + "');" +
            "el.dispatchEvent(new Event('input', {bubbles:true}));");

        org.openqa.selenium.WebElement cantidad2 = driver.findElement(By.id("cantidadPersonas"));
        cantidad2.clear();
        cantidad2.sendKeys("2");

        ((JavascriptExecutor) driver).executeScript(
            "var btn = document.querySelector('button.btn--primary');" +
            "btn.scrollIntoView({block:'center'}); btn.click();");
        Thread.sleep(3000);
        System.out.println("URL después de reserva 2: " + driver.getCurrentUrl());
        System.out.println("Contenido: " + driver.findElement(By.tagName("body")).getText().substring(0, 300));

        
        System.out.println("\n CASO 1 COMPLETADO");
    }
}
