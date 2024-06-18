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
 * <p>UserDefinedAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="UserDefinedAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="CustomCommandAction" type="{}CustomCommandAction"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserDefinedAction", propOrder = {
    "customCommandAction"
})
public class UserDefinedAction {

    @XmlElement(name = "CustomCommandAction", required = true)
    protected CustomCommandAction customCommandAction;

    /**
     * ��ȡcustomCommandAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CustomCommandAction }
     *     
     */
    public CustomCommandAction getCustomCommandAction() {
        return customCommandAction;
    }

    /**
     * ����customCommandAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CustomCommandAction }
     *     
     */
    public void setCustomCommandAction(CustomCommandAction value) {
        this.customCommandAction = value;
    }

}
