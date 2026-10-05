package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.example.utils.WaitUtils.waitForElements;

public class FormFieldsPage extends BasePage {

    @FindBy(xpath = "//label[normalize-space()='Automation tools']/following-sibling::ul[1]/li")
    private List<WebElement> automationTools;

    @FindBy(id = "message")
    private WebElement messageField;

    public FormFieldsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Получить Automation Tools через запятую")
    public String getAutomationToolsAsText() {
        List<WebElement> tools = waitForElements(driver, automationTools);

        if (tools.isEmpty()) {
            throw new IllegalStateException("Automation Tools не найдены");
        }

        StringBuilder result = new StringBuilder();

        for (WebElement tool : tools) {
            String text = tool.getText().trim();

            if (!text.isEmpty()) {
                if (result.length() > 0) {
                    result.append(", ");
                }

                result.append(text);
            }
        }

        if (result.length() == 0) {
            throw new IllegalStateException("Automation Tools не найдены");
        }

        return result.toString();
    }
}