//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Storyboard enity
 * 
 *
 * 
 * <pre>
 * complexType name="Storyboard">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Init" type="{}Init"/>
 *         element name="Story" type="{}Story" maxOccurs="unbounded"/>
 *         element name="StopTrigger" type="{}Trigger"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Storyboard", propOrder = {
    "init",
    "story",
    "stopTrigger"
})
public class Storyboard {

    @XmlElement(name = "Init", required = true)
    protected Init init;
    @XmlElement(name = "Story", required = true)
    protected List<Story> story;
    @XmlElement(name = "StopTrigger", required = true)
    protected Trigger stopTrigger;

    /**
     * ��ȡinit���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Init }
     *     
     */
    public Init getInit() {
        return init;
    }

    /**
     * ����init���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Init }
     *     
     */
    public void setInit(Init value) {
        this.init = value;
    }

    /**
     * Gets the value of the story property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the story property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Story }
     * 
     * 
     */
    public List<Story> getStory() {
        if (story == null) {
            story = new ArrayList<Story>();
        }
        return this.story;
    }

    /**
     * ��ȡstopTrigger���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Trigger }
     *     
     */
    public Trigger getStopTrigger() {
        return stopTrigger;
    }

    /**
     * ����stopTrigger���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Trigger }
     *     
     */
    public void setStopTrigger(Trigger value) {
        this.stopTrigger = value;
    }

}
