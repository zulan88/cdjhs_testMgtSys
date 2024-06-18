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
 * <p>Timing enity
 * 
 *
 * 
 * <pre>
 * complexType name="Timing">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="domainAbsoluteRelative" use="required" type="{}ReferenceContext" />
 *       attribute name="offset" use="required" type="{}Double" />
 *       attribute name="scale" use="required" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Timing")
public class Timing {

    @XmlAttribute(name = "domainAbsoluteRelative", required = true)
    protected String domainAbsoluteRelative;
    @XmlAttribute(name = "offset", required = true)
    protected String offset;
    @XmlAttribute(name = "scale", required = true)
    protected String scale;

    /**
     * ��ȡdomainAbsoluteRelative���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainAbsoluteRelative() {
        return domainAbsoluteRelative;
    }

    /**
     * ����domainAbsoluteRelative���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainAbsoluteRelative(String value) {
        this.domainAbsoluteRelative = value;
    }

    /**
     * ��ȡoffset���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffset() {
        return offset;
    }

    /**
     * ����offset���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffset(String value) {
        this.offset = value;
    }

    /**
     * ��ȡscale���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScale() {
        return scale;
    }

    /**
     * ����scale���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScale(String value) {
        this.scale = value;
    }

}
