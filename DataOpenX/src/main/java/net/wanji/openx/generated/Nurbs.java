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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Nurbs enity
 * 
 *
 * 
 * <pre>
 * complexType name="Nurbs">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="ControlPoint" type="{}ControlPoint" maxOccurs="unbounded" minOccurs="2"/>
 *         element name="Knot" type="{}Knot" maxOccurs="unbounded" minOccurs="2"/>
 *       /sequence>
 *       attribute name="order" use="required" type="{}UnsignedInt" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Nurbs", propOrder = {
    "controlPoint",
    "knot"
})
public class Nurbs {

    @XmlElement(name = "ControlPoint", required = true)
    protected List<ControlPoint> controlPoint;
    @XmlElement(name = "Knot", required = true)
    protected List<Knot> knot;
    @XmlAttribute(name = "order", required = true)
    protected String order;

    /**
     * Gets the value of the controlPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the controlPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getControlPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ControlPoint }
     * 
     * 
     */
    public List<ControlPoint> getControlPoint() {
        if (controlPoint == null) {
            controlPoint = new ArrayList<ControlPoint>();
        }
        return this.controlPoint;
    }

    /**
     * Gets the value of the knot property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the knot property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKnot().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Knot }
     * 
     * 
     */
    public List<Knot> getKnot() {
        if (knot == null) {
            knot = new ArrayList<Knot>();
        }
        return this.knot;
    }

    /**
     * ��ȡorder���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrder() {
        return order;
    }

    /**
     * ����order���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrder(String value) {
        this.order = value;
    }

}
