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
 * <p>GlobalAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="GlobalAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="EnvironmentAction" type="{}EnvironmentAction" minOccurs="0"/>
 *         element name="EntityAction" type="{}EntityAction" minOccurs="0"/>
 *         element name="ParameterAction" type="{}ParameterAction" minOccurs="0"/>
 *         element name="InfrastructureAction" type="{}InfrastructureAction" minOccurs="0"/>
 *         element name="TrafficAction" type="{}TrafficAction" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GlobalAction", propOrder = {
    "environmentAction",
    "entityAction",
    "parameterAction",
    "infrastructureAction",
    "trafficAction"
})
public class GlobalAction {

    @XmlElement(name = "EnvironmentAction")
    protected EnvironmentAction environmentAction;
    @XmlElement(name = "EntityAction")
    protected EntityAction entityAction;
    @XmlElement(name = "ParameterAction")
    protected ParameterAction parameterAction;
    @XmlElement(name = "InfrastructureAction")
    protected InfrastructureAction infrastructureAction;
    @XmlElement(name = "TrafficAction")
    protected TrafficAction trafficAction;

    /**
     * ��ȡenvironmentAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EnvironmentAction }
     *     
     */
    public EnvironmentAction getEnvironmentAction() {
        return environmentAction;
    }

    /**
     * ����environmentAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EnvironmentAction }
     *     
     */
    public void setEnvironmentAction(EnvironmentAction value) {
        this.environmentAction = value;
    }

    /**
     * ��ȡentityAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EntityAction }
     *     
     */
    public EntityAction getEntityAction() {
        return entityAction;
    }

    /**
     * ����entityAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EntityAction }
     *     
     */
    public void setEntityAction(EntityAction value) {
        this.entityAction = value;
    }

    /**
     * ��ȡparameterAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterAction }
     *     
     */
    public ParameterAction getParameterAction() {
        return parameterAction;
    }

    /**
     * ����parameterAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterAction }
     *     
     */
    public void setParameterAction(ParameterAction value) {
        this.parameterAction = value;
    }

    /**
     * ��ȡinfrastructureAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link InfrastructureAction }
     *     
     */
    public InfrastructureAction getInfrastructureAction() {
        return infrastructureAction;
    }

    /**
     * ����infrastructureAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link InfrastructureAction }
     *     
     */
    public void setInfrastructureAction(InfrastructureAction value) {
        this.infrastructureAction = value;
    }

    /**
     * ��ȡtrafficAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficAction }
     *     
     */
    public TrafficAction getTrafficAction() {
        return trafficAction;
    }

    /**
     * ����trafficAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficAction }
     *     
     */
    public void setTrafficAction(TrafficAction value) {
        this.trafficAction = value;
    }

}
