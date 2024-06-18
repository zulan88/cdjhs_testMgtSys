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
 * <p>ParameterAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="ParameterAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="SetAction" type="{}ParameterSetAction" minOccurs="0"/>
 *         element name="ModifyAction" type="{}ParameterModifyAction" minOccurs="0"/>
 *       /choice>
 *       attribute name="parameterRef" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterAction", propOrder = {
    "setAction",
    "modifyAction"
})
public class ParameterAction {

    @XmlElement(name = "SetAction")
    protected ParameterSetAction setAction;
    @XmlElement(name = "ModifyAction")
    protected ParameterModifyAction modifyAction;
    @XmlAttribute(name = "parameterRef", required = true)
    protected String parameterRef;

    /**
     * ��ȡsetAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterSetAction }
     *     
     */
    public ParameterSetAction getSetAction() {
        return setAction;
    }

    /**
     * ����setAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterSetAction }
     *     
     */
    public void setSetAction(ParameterSetAction value) {
        this.setAction = value;
    }

    /**
     * ��ȡmodifyAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterModifyAction }
     *     
     */
    public ParameterModifyAction getModifyAction() {
        return modifyAction;
    }

    /**
     * ����modifyAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterModifyAction }
     *     
     */
    public void setModifyAction(ParameterModifyAction value) {
        this.modifyAction = value;
    }

    /**
     * ��ȡparameterRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParameterRef() {
        return parameterRef;
    }

    /**
     * ����parameterRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParameterRef(String value) {
        this.parameterRef = value;
    }

}
