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
 * <p>ParameterModifyAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="ParameterModifyAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Rule" type="{}ModifyRule"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterModifyAction", propOrder = {

})
public class ParameterModifyAction {

    @XmlElement(name = "Rule", required = true)
    protected ModifyRule rule;

    /**
     * ��ȡrule���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ModifyRule }
     *     
     */
    public ModifyRule getRule() {
        return rule;
    }

    /**
     * ����rule���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ModifyRule }
     *     
     */
    public void setRule(ModifyRule value) {
        this.rule = value;
    }

}
