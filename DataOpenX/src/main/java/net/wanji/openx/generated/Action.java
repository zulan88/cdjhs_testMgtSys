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
 * <p>Action enity
 * 
 *
 * 
 * <pre>
 * complexType name="Action">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="GlobalAction" type="{}GlobalAction" minOccurs="0"/>
 *         element name="UserDefinedAction" type="{}UserDefinedAction" minOccurs="0"/>
 *         element name="PrivateAction" type="{}PrivateAction" minOccurs="0"/>
 *       /choice>
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Action", propOrder = {
    "globalAction",
    "userDefinedAction",
    "privateAction"
})
public class Action {

    @XmlElement(name = "GlobalAction")
    protected GlobalAction globalAction;
    @XmlElement(name = "UserDefinedAction")
    protected UserDefinedAction userDefinedAction;
    @XmlElement(name = "PrivateAction")
    protected PrivateAction privateAction;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡglobalAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link GlobalAction }
     *     
     */
    public GlobalAction getGlobalAction() {
        return globalAction;
    }

    /**
     * ����globalAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link GlobalAction }
     *     
     */
    public void setGlobalAction(GlobalAction value) {
        this.globalAction = value;
    }

    /**
     * ��ȡuserDefinedAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link UserDefinedAction }
     *     
     */
    public UserDefinedAction getUserDefinedAction() {
        return userDefinedAction;
    }

    /**
     * ����userDefinedAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link UserDefinedAction }
     *     
     */
    public void setUserDefinedAction(UserDefinedAction value) {
        this.userDefinedAction = value;
    }

    /**
     * ��ȡprivateAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PrivateAction }
     *     
     */
    public PrivateAction getPrivateAction() {
        return privateAction;
    }

    /**
     * ����privateAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PrivateAction }
     *     
     */
    public void setPrivateAction(PrivateAction value) {
        this.privateAction = value;
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
