//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>BoundingBox enity
 * 
 *
 * 
 * <pre>
 * complexType name="BoundingBox">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Center" type="{}Center"/>
 *         element name="Dimensions" type="{}Dimensions"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoundingBox", propOrder = {

})
public class BoundingBox {

    public BoundingBox(){}

    public BoundingBox(String type){
        this.center = new Center(type);
        this.dimensions = new Dimensions(type);
    }

    @XmlElement(name = "Center", required = true)
    protected Center center;
    @XmlElement(name = "Dimensions", required = true)
    protected Dimensions dimensions;

    /**
     * ��ȡcenter���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Center }
     *     
     */
    public Center getCenter() {
        return center;
    }

    /**
     * ����center���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Center }
     *     
     */
    public void setCenter(Center value) {
        this.center = value;
    }

    /**
     * ��ȡdimensions���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Dimensions }
     *     
     */
    public Dimensions getDimensions() {
        return dimensions;
    }

    /**
     * ����dimensions���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Dimensions }
     *     
     */
    public void setDimensions(Dimensions value) {
        this.dimensions = value;
    }

}
