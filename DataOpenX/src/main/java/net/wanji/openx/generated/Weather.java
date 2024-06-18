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
 * <p>Weather enity
 * 
 *
 * 
 * <pre>
 * complexType name="Weather">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Sun" type="{}Sun"/>
 *         element name="Fog" type="{}Fog"/>
 *         element name="Precipitation" type="{}Precipitation"/>
 *       /all>
 *       attribute name="cloudState" use="required" type="{}CloudState" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Weather", propOrder = {

})
public class Weather {

    @XmlElement(name = "Sun", required = true)
    protected Sun sun;
    @XmlElement(name = "Fog", required = true)
    protected Fog fog;
    @XmlElement(name = "Precipitation", required = true)
    protected Precipitation precipitation;
    @XmlAttribute(name = "cloudState", required = true)
    protected String cloudState;

    /**
     * ��ȡsun���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Sun }
     *     
     */
    public Sun getSun() {
        return sun;
    }

    /**
     * ����sun���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Sun }
     *     
     */
    public void setSun(Sun value) {
        this.sun = value;
    }

    /**
     * ��ȡfog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Fog }
     *     
     */
    public Fog getFog() {
        return fog;
    }

    /**
     * ����fog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Fog }
     *     
     */
    public void setFog(Fog value) {
        this.fog = value;
    }

    /**
     * ��ȡprecipitation���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Precipitation }
     *     
     */
    public Precipitation getPrecipitation() {
        return precipitation;
    }

    /**
     * ����precipitation���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Precipitation }
     *     
     */
    public void setPrecipitation(Precipitation value) {
        this.precipitation = value;
    }

    /**
     * ��ȡcloudState���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloudState() {
        return cloudState;
    }

    /**
     * ����cloudState���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloudState(String value) {
        this.cloudState = value;
    }

}
