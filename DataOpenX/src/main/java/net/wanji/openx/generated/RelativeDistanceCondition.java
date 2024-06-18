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
 * <p>RelativeDistanceCondition enity
 * 
 *
 * 
 * <pre>
 * complexType name="RelativeDistanceCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="entityRef" use="required" type="{}String" />
 *       attribute name="freespace" use="required" type="{}Boolean" />
 *       attribute name="relativeDistanceType" use="required" type="{}RelativeDistanceType" />
 *       attribute name="rule" use="required" type="{}Rule" />
 *       attribute name="value" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelativeDistanceCondition")
public class RelativeDistanceCondition {

    @XmlAttribute(name = "entityRef", required = true)
    protected String entityRef;
    @XmlAttribute(name = "freespace", required = true)
    protected String freespace;
    @XmlAttribute(name = "relativeDistanceType", required = true)
    protected String relativeDistanceType;
    @XmlAttribute(name = "rule", required = true)
    protected String rule;
    @XmlAttribute(name = "value", required = true)
    protected String value;

    /**
     * ��ȡentityRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityRef() {
        return entityRef;
    }

    /**
     * ����entityRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityRef(String value) {
        this.entityRef = value;
    }

    /**
     * ��ȡfreespace���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreespace() {
        return freespace;
    }

    /**
     * ����freespace���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreespace(String value) {
        this.freespace = value;
    }

    /**
     * ��ȡrelativeDistanceType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRelativeDistanceType() {
        return relativeDistanceType;
    }

    /**
     * ����relativeDistanceType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRelativeDistanceType(String value) {
        this.relativeDistanceType = value;
    }

    /**
     * ��ȡrule���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRule() {
        return rule;
    }

    /**
     * ����rule���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRule(String value) {
        this.rule = value;
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
