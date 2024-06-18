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
 * <p>ControllerAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="ControllerAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="AssignControllerAction" type="{}AssignControllerAction"/>
 *         element name="OverrideControllerValueAction" type="{}OverrideControllerValueAction"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControllerAction", propOrder = {

})
public class ControllerAction {

    @XmlElement(name = "AssignControllerAction", required = true)
    protected AssignControllerAction assignControllerAction;
    @XmlElement(name = "OverrideControllerValueAction", required = true)
    protected OverrideControllerValueAction overrideControllerValueAction;

    /**
     * ��ȡassignControllerAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AssignControllerAction }
     *     
     */
    public AssignControllerAction getAssignControllerAction() {
        return assignControllerAction;
    }

    /**
     * ����assignControllerAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AssignControllerAction }
     *     
     */
    public void setAssignControllerAction(AssignControllerAction value) {
        this.assignControllerAction = value;
    }

    /**
     * ��ȡoverrideControllerValueAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideControllerValueAction }
     *     
     */
    public OverrideControllerValueAction getOverrideControllerValueAction() {
        return overrideControllerValueAction;
    }

    /**
     * ����overrideControllerValueAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideControllerValueAction }
     *     
     */
    public void setOverrideControllerValueAction(OverrideControllerValueAction value) {
        this.overrideControllerValueAction = value;
    }

}
