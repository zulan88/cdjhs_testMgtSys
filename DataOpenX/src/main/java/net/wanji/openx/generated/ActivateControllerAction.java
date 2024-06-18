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
 * <p>ActivateControllerAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="ActivateControllerAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="lateral" type="{}Boolean" />
 *       attribute name="longitudinal" type="{}Boolean" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActivateControllerAction")
public class ActivateControllerAction {

    @XmlAttribute(name = "lateral")
    protected String lateral;
    @XmlAttribute(name = "longitudinal")
    protected String longitudinal;

    /**
     * ��ȡlateral���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLateral() {
        return lateral;
    }

    /**
     * ����lateral���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLateral(String value) {
        this.lateral = value;
    }

    /**
     * ��ȡlongitudinal���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongitudinal() {
        return longitudinal;
    }

    /**
     * ����longitudinal���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongitudinal(String value) {
        this.longitudinal = value;
    }

}
