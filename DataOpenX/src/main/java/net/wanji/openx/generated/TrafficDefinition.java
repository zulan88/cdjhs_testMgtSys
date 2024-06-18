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
 * <p>TrafficDefinition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="TrafficDefinition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="VehicleCategoryDistribution" type="{}VehicleCategoryDistribution"/>
 *         element name="ControllerDistribution" type="{}ControllerDistribution"/>
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
@XmlType(name = "TrafficDefinition", propOrder = {

})
public class TrafficDefinition {

    @XmlElement(name = "VehicleCategoryDistribution", required = true)
    protected VehicleCategoryDistribution vehicleCategoryDistribution;
    @XmlElement(name = "ControllerDistribution", required = true)
    protected ControllerDistribution controllerDistribution;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡvehicleCategoryDistribution���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link VehicleCategoryDistribution }
     *     
     */
    public VehicleCategoryDistribution getVehicleCategoryDistribution() {
        return vehicleCategoryDistribution;
    }

    /**
     * ����vehicleCategoryDistribution���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleCategoryDistribution }
     *     
     */
    public void setVehicleCategoryDistribution(VehicleCategoryDistribution value) {
        this.vehicleCategoryDistribution = value;
    }

    /**
     * ��ȡcontrollerDistribution���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ControllerDistribution }
     *     
     */
    public ControllerDistribution getControllerDistribution() {
        return controllerDistribution;
    }

    /**
     * ����controllerDistribution���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ControllerDistribution }
     *     
     */
    public void setControllerDistribution(ControllerDistribution value) {
        this.controllerDistribution = value;
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
