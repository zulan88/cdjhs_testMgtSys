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
 * <p>Precipitation enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Precipitation">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="intensity" use="required" type="{}Double" />
 *       attribute name="precipitationType" use="required" type="{}PrecipitationType" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Precipitation")
public class Precipitation {

    @XmlAttribute(name = "intensity", required = true)
    protected String intensity;
    @XmlAttribute(name = "precipitationType", required = true)
    protected String precipitationType;

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

    /**
     * ��ȡprecipitationType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrecipitationType() {
        return precipitationType;
    }

    /**
     * ����precipitationType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrecipitationType(String value) {
        this.precipitationType = value;
    }

}
