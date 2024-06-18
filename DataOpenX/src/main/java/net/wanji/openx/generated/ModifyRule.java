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
 * <p>ModifyRule enity
 * 
 *
 * 
 * <pre>
 * complexType name="ModifyRule">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="AddValue" type="{}ParameterAddValueRule" minOccurs="0"/>
 *         element name="MultiplyByValue" type="{}ParameterMultiplyByValueRule" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyRule", propOrder = {
    "addValue",
    "multiplyByValue"
})
public class ModifyRule {

    @XmlElement(name = "AddValue")
    protected ParameterAddValueRule addValue;
    @XmlElement(name = "MultiplyByValue")
    protected ParameterMultiplyByValueRule multiplyByValue;

    /**
     * ��ȡaddValue���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterAddValueRule }
     *     
     */
    public ParameterAddValueRule getAddValue() {
        return addValue;
    }

    /**
     * ����addValue���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterAddValueRule }
     *     
     */
    public void setAddValue(ParameterAddValueRule value) {
        this.addValue = value;
    }

    /**
     * ��ȡmultiplyByValue���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterMultiplyByValueRule }
     *     
     */
    public ParameterMultiplyByValueRule getMultiplyByValue() {
        return multiplyByValue;
    }

    /**
     * ����multiplyByValue���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterMultiplyByValueRule }
     *     
     */
    public void setMultiplyByValue(ParameterMultiplyByValueRule value) {
        this.multiplyByValue = value;
    }

}
