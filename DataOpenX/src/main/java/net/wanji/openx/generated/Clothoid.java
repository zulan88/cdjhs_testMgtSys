//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clothoid enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Clothoid">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Position" type="{}Position"/>
 *       /sequence>
 *       attribute name="curvature" use="required" type="{}Double" />
 *       attribute name="curvatureDot" use="required" type="{}Double" />
 *       attribute name="length" use="required" type="{}Double" />
 *       attribute name="startTime" type="{}Double" />
 *       attribute name="stopTime" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Clothoid", propOrder = {
    "position"
})
public class Clothoid {

    @XmlElement(name = "Position", required = true)
    protected Position position;
    @XmlAttribute(name = "curvature", required = true)
    protected String curvature;
    @XmlAttribute(name = "curvatureDot", required = true)
    protected String curvatureDot;
    @XmlAttribute(name = "length", required = true)
    protected String length;
    @XmlAttribute(name = "startTime")
    protected String startTime;
    @XmlAttribute(name = "stopTime")
    protected String stopTime;

    /**
     * ��ȡposition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getPosition() {
        return position;
    }

    /**
     * ����position���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setPosition(Position value) {
        this.position = value;
    }

    /**
     * ��ȡcurvature���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurvature() {
        return curvature;
    }

    /**
     * ����curvature���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurvature(String value) {
        this.curvature = value;
    }

    /**
     * ��ȡcurvatureDot���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurvatureDot() {
        return curvatureDot;
    }

    /**
     * ����curvatureDot���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurvatureDot(String value) {
        this.curvatureDot = value;
    }

    /**
     * ��ȡlength���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLength() {
        return length;
    }

    /**
     * ����length���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLength(String value) {
        this.length = value;
    }

    /**
     * ��ȡstartTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * ����startTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * ��ȡstopTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStopTime() {
        return stopTime;
    }

    /**
     * ����stopTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStopTime(String value) {
        this.stopTime = value;
    }

}
