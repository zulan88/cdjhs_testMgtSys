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
 * <p>RelativeWorldPosition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="RelativeWorldPosition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Orientation" type="{}Orientation" minOccurs="0"/>
 *       /all>
 *       attribute name="entityRef" use="required" type="{}String" />
 *       attribute name="dx" use="required" type="{}Double" />
 *       attribute name="dy" use="required" type="{}Double" />
 *       attribute name="dz" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelativeWorldPosition", propOrder = {

})
public class RelativeWorldPosition {

    @XmlElement(name = "Orientation")
    protected Orientation orientation;
    @XmlAttribute(name = "entityRef", required = true)
    protected String entityRef;
    @XmlAttribute(name = "dx", required = true)
    protected String dx;
    @XmlAttribute(name = "dy", required = true)
    protected String dy;
    @XmlAttribute(name = "dz")
    protected String dz;

    /**
     * ��ȡorientation���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Orientation }
     *     
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * ����orientation���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Orientation }
     *     
     */
    public void setOrientation(Orientation value) {
        this.orientation = value;
    }

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
     * ��ȡdx���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDx() {
        return dx;
    }

    /**
     * ����dx���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDx(String value) {
        this.dx = value;
    }

    /**
     * ��ȡdy���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDy() {
        return dy;
    }

    /**
     * ����dy���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDy(String value) {
        this.dy = value;
    }

    /**
     * ��ȡdz���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDz() {
        return dz;
    }

    /**
     * ����dz���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDz(String value) {
        this.dz = value;
    }

}
