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
 * <p>Axles enity
 *
 *
 *
 * <pre>
 * complexType name="Axles">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="FrontAxle" type="{}Axle"/>
 *         element name="RearAxle" type="{}Axle"/>
 *         element name="AdditionalAxle" type="{}Axle" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Axles", propOrder = {
        "frontAxle",
        "rearAxle",
        "additionalAxle"
})
public class Axles {

    public Axles() {
    }

    public Axles(String type) {
        this.frontAxle = new Axle(type + "F");
        this.rearAxle = new Axle(type + "R");
    }

    @XmlElement(name = "FrontAxle", required = true)
    protected Axle frontAxle;
    @XmlElement(name = "RearAxle", required = true)
    protected Axle rearAxle;
    @XmlElement(name = "AdditionalAxle")
    protected List<Axle> additionalAxle;

    /**
     * ��ȡfrontAxle���Ե�ֵ��
     *
     * @return possible object is
     * {@link Axle }
     */
    public Axle getFrontAxle() {
        return frontAxle;
    }

    /**
     * ����frontAxle���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link Axle }
     */
    public void setFrontAxle(Axle value) {
        this.frontAxle = value;
    }

    /**
     * ��ȡrearAxle���Ե�ֵ��
     *
     * @return possible object is
     * {@link Axle }
     */
    public Axle getRearAxle() {
        return rearAxle;
    }

    /**
     * ����rearAxle���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link Axle }
     */
    public void setRearAxle(Axle value) {
        this.rearAxle = value;
    }

    /**
     * Gets the value of the additionalAxle property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the additionalAxle property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdditionalAxle().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Axle }
     */
    public List<Axle> getAdditionalAxle() {
        if (additionalAxle == null) {
            additionalAxle = new ArrayList<Axle>();
        }
        return this.additionalAxle;
    }

}
