import org.openqa.selenium.By
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.WebClient

this.metaClass.mixin(cuke4duke.GroovyDsl)


Given(~"I am on the app default page") { ->
    page = webClient.getPage("http://localhost:8080/cucumber/");
    assertEquals 200, page.webResponse.statusCode
}

Then(~"I should see only") { String text ->
  assertEquals text, page.webResponse.contentAsString
}
