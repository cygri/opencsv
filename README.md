OpenCSV fork
===================
Implemented ability to map CSV to Bean by annotating the relevant fields in the target Bean class.

Example
-------------
Consider the following Java Bean:

~~~~~~
import com.opencsv.bean.CsvBind;
import java.util.Date;

public class SimpleAnnotatedMockBean {
	@CsvBind
	private String name;
	@CsvBind
	private String orderNumber;
	@CsvBind(required = true)
	private int num;
	private Date someOtherField;
	
    public String getName() {
        return name;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public int getNum() {
        return num;
    }
}
~~~~~~

And the following code:

~~~~~~
HeaderColumnNameMappingStrategy<SimpleAnnotatedMockBean> strategy = new HeaderColumnNameMappingStrategy<>();
strategy.setType(SimpleAnnotatedMockBean.class);
CsvToBean<SimpleAnnotatedMockBean> csvToBean = new CsvToBean<>();
List<SimpleAnnotatedMockBean> beanList = csvToBean.parse(strategy, createReader());
~~~~~~

The resulting **beanList** variable will contain a List of **SimpleAnnotatedMockBean** objects with the **name**, **orderNumber** and **num** fields populated. The **someOtherField** property will not have been populated, because it was not annotated with **@CsvBind**.

Notes
-------------
* You cannot combine setter-based Bean-binding and annotation-based Bean-binding. If there is a **@CsvBind** annotation present, annotation-based Bean-binding takes presedence. If there are no **@CsvBind** annotations present, setter-based Bean-binding will be attempted.
* Annotation-based binding is only supported for primitive and String types.