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
 * <p>ByValueCondition enity
 * 
 *
 * 
 * <pre>
 * complexType name="ByValueCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="ParameterCondition" type="{}ParameterCondition" minOccurs="0"/>
 *         element name="TimeOfDayCondition" type="{}TimeOfDayCondition" minOccurs="0"/>
 *         element name="SimulationTimeCondition" type="{}SimulationTimeCondition" minOccurs="0"/>
 *         element name="StoryboardElementStateCondition" type="{}StoryboardElementStateCondition" minOccurs="0"/>
 *         element name="UserDefinedValueCondition" type="{}UserDefinedValueCondition" minOccurs="0"/>
 *         element name="TrafficSignalCondition" type="{}TrafficSignalCondition" minOccurs="0"/>
 *         element name="TrafficSignalControllerCondition" type="{}TrafficSignalControllerCondition" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ByValueCondition", propOrder = {
    "parameterCondition",
    "timeOfDayCondition",
    "simulationTimeCondition",
    "storyboardElementStateCondition",
    "userDefinedValueCondition",
    "trafficSignalCondition",
    "trafficSignalControllerCondition"
})
public class ByValueCondition {

    @XmlElement(name = "ParameterCondition")
    protected ParameterCondition parameterCondition;
    @XmlElement(name = "TimeOfDayCondition")
    protected TimeOfDayCondition timeOfDayCondition;
    @XmlElement(name = "SimulationTimeCondition")
    protected SimulationTimeCondition simulationTimeCondition;
    @XmlElement(name = "StoryboardElementStateCondition")
    protected StoryboardElementStateCondition storyboardElementStateCondition;
    @XmlElement(name = "UserDefinedValueCondition")
    protected UserDefinedValueCondition userDefinedValueCondition;
    @XmlElement(name = "TrafficSignalCondition")
    protected TrafficSignalCondition trafficSignalCondition;
    @XmlElement(name = "TrafficSignalControllerCondition")
    protected TrafficSignalControllerCondition trafficSignalControllerCondition;

    /**
     * ��ȡparameterCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterCondition }
     *     
     */
    public ParameterCondition getParameterCondition() {
        return parameterCondition;
    }

    /**
     * ����parameterCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterCondition }
     *     
     */
    public void setParameterCondition(ParameterCondition value) {
        this.parameterCondition = value;
    }

    /**
     * ��ȡtimeOfDayCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TimeOfDayCondition }
     *     
     */
    public TimeOfDayCondition getTimeOfDayCondition() {
        return timeOfDayCondition;
    }

    /**
     * ����timeOfDayCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TimeOfDayCondition }
     *     
     */
    public void setTimeOfDayCondition(TimeOfDayCondition value) {
        this.timeOfDayCondition = value;
    }

    /**
     * ��ȡsimulationTimeCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SimulationTimeCondition }
     *     
     */
    public SimulationTimeCondition getSimulationTimeCondition() {
        return simulationTimeCondition;
    }

    /**
     * ����simulationTimeCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SimulationTimeCondition }
     *     
     */
    public void setSimulationTimeCondition(SimulationTimeCondition value) {
        this.simulationTimeCondition = value;
    }

    /**
     * ��ȡstoryboardElementStateCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link StoryboardElementStateCondition }
     *     
     */
    public StoryboardElementStateCondition getStoryboardElementStateCondition() {
        return storyboardElementStateCondition;
    }

    /**
     * ����storyboardElementStateCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link StoryboardElementStateCondition }
     *     
     */
    public void setStoryboardElementStateCondition(StoryboardElementStateCondition value) {
        this.storyboardElementStateCondition = value;
    }

    /**
     * ��ȡuserDefinedValueCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link UserDefinedValueCondition }
     *     
     */
    public UserDefinedValueCondition getUserDefinedValueCondition() {
        return userDefinedValueCondition;
    }

    /**
     * ����userDefinedValueCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link UserDefinedValueCondition }
     *     
     */
    public void setUserDefinedValueCondition(UserDefinedValueCondition value) {
        this.userDefinedValueCondition = value;
    }

    /**
     * ��ȡtrafficSignalCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSignalCondition }
     *     
     */
    public TrafficSignalCondition getTrafficSignalCondition() {
        return trafficSignalCondition;
    }

    /**
     * ����trafficSignalCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSignalCondition }
     *     
     */
    public void setTrafficSignalCondition(TrafficSignalCondition value) {
        this.trafficSignalCondition = value;
    }

    /**
     * ��ȡtrafficSignalControllerCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSignalControllerCondition }
     *     
     */
    public TrafficSignalControllerCondition getTrafficSignalControllerCondition() {
        return trafficSignalControllerCondition;
    }

    /**
     * ����trafficSignalControllerCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSignalControllerCondition }
     *     
     */
    public void setTrafficSignalControllerCondition(TrafficSignalControllerCondition value) {
        this.trafficSignalControllerCondition = value;
    }

}
