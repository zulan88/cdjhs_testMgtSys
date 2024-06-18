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
 * <p>Shape enity
 * 
 *
 * 
 * <pre>
 * complexType name="Shape">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="Polyline" type="{}Polyline" minOccurs="0"/>
 *         element name="Clothoid" type="{}Clothoid" minOccurs="0"/>
 *         element name="Nurbs" type="{}Nurbs" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Shape", propOrder = {
    "polyline",
    "clothoid",
    "nurbs"
})
public class Shape {

    @XmlElement(name = "Polyline")
    protected Polyline polyline;
    @XmlElement(name = "Clothoid")
    protected Clothoid clothoid;
    @XmlElement(name = "Nurbs")
    protected Nurbs nurbs;

    /**
     * ��ȡpolyline���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Polyline }
     *     
     */
    public Polyline getPolyline() {
        return polyline;
    }

    /**
     * ����polyline���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Polyline }
     *     
     */
    public void setPolyline(Polyline value) {
        this.polyline = value;
    }

    /**
     * ��ȡclothoid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Clothoid }
     *     
     */
    public Clothoid getClothoid() {
        return clothoid;
    }

    /**
     * ����clothoid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Clothoid }
     *     
     */
    public void setClothoid(Clothoid value) {
        this.clothoid = value;
    }

    /**
     * ��ȡnurbs���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Nurbs }
     *     
     */
    public Nurbs getNurbs() {
        return nurbs;
    }

    /**
     * ����nurbs���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Nurbs }
     *     
     */
    public void setNurbs(Nurbs value) {
        this.nurbs = value;
    }

}
