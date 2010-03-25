//import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import com.gargoylesoftware.htmlunit.WebClient

this.metaClass.mixin(cuke4duke.GroovyDsl)

Before() {
    browser = new HtmlUnitDriver()
    webClient = new WebClient()
}

After() {
  webClient.closeAllWindows()
  browser.close()
  browser.quit()
}