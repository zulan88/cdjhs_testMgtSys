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
 * <p>Environment enity
 * 
 *
 * 
 * <pre>
 * complexType name="Environment">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="ParameterDeclarations" type="{}ParameterDeclarations" minOccurs="0"/>
 *         element name="TimeOfDay" type="{}TimeOfDay"/>
 *         element name="Weather" type="{}Weather"/>
 *         element name="RoadCondition" type="{}RoadCondition"/>
 *       /all>
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Environment", propOrder = {

})
public class Environment {

    @XmlElement(name = "ParameterDeclarations")
    protected ParameterDeclarations parameterDeclarations;
    @XmlElement(name = "TimeOfDay", required = true)
    protected TimeOfDay timeOfDay;
    @XmlElement(name = "Weather", required = true)
    protected Weather weather;
    @XmlElement(name = "RoadCondition", required = true)
    protected RoadCondition roadCondition;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡparameterDeclarations���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterDeclarations }
     *     
     */
    public ParameterDeclarations getParameterDeclarations() {
        return parameterDeclarations;
    }

    /**
     * ����parameterDeclarations���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterDeclarations }
     *     
     */
    public void setParameterDeclarations(ParameterDeclarations value) {
        this.parameterDeclarations = value;
    }

    /**
     * ��ȡtimeOfDay���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TimeOfDay }
     *     
     */
    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    /**
     * ����timeOfDay���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TimeOfDay }
     *     
     */
    public void setTimeOfDay(TimeOfDay value) {
        this.timeOfDay = value;
    }

    /**
     * ��ȡweather���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Weather }
     *     
     */
    public Weather getWeather() {
        return weather;
    }

    /**
     * ����weather���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Weather }
     *     
     */
    public void setWeather(Weather value) {
        this.weather = value;
    }

    /**
     * ��ȡroadCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RoadCondition }
     *     
     */
    public RoadCondition getRoadCondition() {
        return roadCondition;
    }

    /**
     * ����roadCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RoadCondition }
     *     
     */
    public void setRoadCondition(RoadCondition value) {
        this.roadCondition = value;
    }

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
