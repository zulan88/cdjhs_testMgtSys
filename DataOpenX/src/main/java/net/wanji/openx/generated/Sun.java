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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Sun enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Sun">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="azimuth" use="required" type="{}Double" />
 *       attribute name="elevation" use="required" type="{}Double" />
 *       attribute name="intensity" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sun")
public class Sun {

    @XmlAttribute(name = "azimuth", required = true)
    protected String azimuth;
    @XmlAttribute(name = "elevation", required = true)
    protected String elevation;
    @XmlAttribute(name = "intensity", required = true)
    protected String intensity;

    /**
     * ��ȡazimuth���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAzimuth() {
        return azimuth;
    }

    /**
     * ����azimuth���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAzimuth(String value) {
        this.azimuth = value;
    }

    /**
     * ��ȡelevation���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElevation() {
        return elevation;
    }

    /**
     * ����elevation���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElevation(String value) {
        this.elevation = value;
    }

    /**
     * ��ȡintensity���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntensity() {
        return intensity;
    }

    /**
     * ����intensity���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntensity(String value) {
        this.intensity = value;
    }

}
