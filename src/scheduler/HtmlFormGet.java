package scheduler;
import java.util.List;

/**
 * HtmlGet obtains form names on a website and fills in select and text fields
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

/**
 * HtmlFormGet represents filling out an HtmlForm
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class HtmlFormGet {
    
    private WebClient webClient;
    private HtmlPage page;
    private HtmlForm form;
    
    /**
     * Constructor that take the url page a form is on and the name of the form
     * 
     * @param url link to the html page
     * @param formName name of the html form
     * @throws Exception
     */
    public HtmlFormGet(String url, String formName) throws Exception {
        webClient = new WebClient();
        page = webClient.getPage(url);
        form = page.getFormByName(formName);
    }
    
    /**
     * Fills a text field on the form
     * 
     * @param fieldName name of the field
     * @param value value to be set
     */
    public void fillTextField(String fieldName, String value) {
        HtmlTextInput field = form.getInputByName(fieldName);
        field.setValueAttribute(value);
    }
    
    /**
     * Fills a Select field on the form
     * 
     * @param fieldName name of the field
     * @param value value to be set
     */
    public void fillSelectField(String fieldName, String value) {
        HtmlSelect field = form.getSelectByName(fieldName);
        field.setSelectedAttribute(value, true);
    }
    
    /**
     * fills the select using the specified index
     * 
     * @param fieldName the name of the select
     * @param index the index to be chosen
     */
    public void fillSelectField(String fieldName, int index) {
        HtmlSelect field = form.getSelectByName(fieldName);
        List<HtmlOption> list = field.getOptions();
        field.setSelectedAttribute(list.get(index), true);
    }
    
    /**
     * The option names for a Select Field
     * 
     * @param fieldName name of the select
     * @return array containing the names
     */
    public String[] getSelectOptions(String fieldName) {
        HtmlSelect field = form.getSelectByName(fieldName);
        String[] options = new String[field.getOptions().size()];
        for (int i = 0; i < options.length; i++) {
            options[i] = field.getOptions().get(i).asText();
        }
        return options;
    }
    
    /**
     * The options values for a Select Field
     * 
     * @param fieldName name of the select
     * @return array containing the values
     */
    public String[] getSelectOptionValues(String fieldName) {
        HtmlSelect field = form.getSelectByName(fieldName);
        String[] options = new String[getSelectOptions(fieldName).length];
        for (int i = 0; i < options.length; i++) {
            options[i] = field.getOptions().get(i).getAttribute("value");
        }
        return options;
    }
    
    /**
     * Presses a button on the form
     * 
     * @param buttonName name of the button
     * @return the HtmlPage of that is resulted
     * @throws Exception
     */
    public HtmlPage pressButton(String buttonValue) throws Exception {
        HtmlSubmitInput button = form.getInputByValue(buttonValue);
        return button.click();
    }
    
    /**
     * The HtmlSelect in the form
     * 
     * @param name name of the Select
     * @return HtmlSelect object
     */
    public HtmlSelect getSelectField(String name) {
        return form.getSelectByName(name);
    }
}
