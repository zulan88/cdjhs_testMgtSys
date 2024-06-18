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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TransitionDynamics enity
 * 
 * 
 * 
 * <pre>
 * complexType name="TransitionDynamics">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="dynamicsDimension" use="required" type="{}DynamicsDimension" />
 *       attribute name="dynamicsShape" use="required" type="{}DynamicsShape" />
 *       attribute name="value" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransitionDynamics")
public class TransitionDynamics {

    @XmlAttribute(name = "dynamicsDimension", required = true)
    protected String dynamicsDimension;
    @XmlAttribute(name = "dynamicsShape", required = true)
    protected String dynamicsShape;
    @XmlAttribute(name = "value", required = true)
    protected String value;

    /**
     * ��ȡdynamicsDimension���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicsDimension() {
        return dynamicsDimension;
    }

    /**
     * ����dynamicsDimension���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicsDimension(String value) {
        this.dynamicsDimension = value;
    }

    /**
     * ��ȡdynamicsShape���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicsShape() {
        return dynamicsShape;
    }

    /**
     * ����dynamicsShape���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicsShape(String value) {
        this.dynamicsShape = value;
    }

    /**
     * ��ȡvalue���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * ����value���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
