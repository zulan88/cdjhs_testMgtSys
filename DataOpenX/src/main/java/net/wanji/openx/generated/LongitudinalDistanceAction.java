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
 * <p>LongitudinalDistanceAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="LongitudinalDistanceAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="DynamicConstraints" type="{}DynamicConstraints" minOccurs="0"/>
 *       /all>
 *       attribute name="entityRef" use="required" type="{}String" />
 *       attribute name="continuous" use="required" type="{}Boolean" />
 *       attribute name="distance" type="{}Double" />
 *       attribute name="freespace" use="required" type="{}Boolean" />
 *       attribute name="timeGap" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LongitudinalDistanceAction", propOrder = {

})
public class LongitudinalDistanceAction {

    @XmlElement(name = "DynamicConstraints")
    protected DynamicConstraints dynamicConstraints;
    @XmlAttribute(name = "entityRef", required = true)
    protected String entityRef;
    @XmlAttribute(name = "continuous", required = true)
    protected String continuous;
    @XmlAttribute(name = "distance")
    protected String distance;
    @XmlAttribute(name = "freespace", required = true)
    protected String freespace;
    @XmlAttribute(name = "timeGap")
    protected String timeGap;

    /**
     * ��ȡdynamicConstraints���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link DynamicConstraints }
     *     
     */
    public DynamicConstraints getDynamicConstraints() {
        return dynamicConstraints;
    }

    /**
     * ����dynamicConstraints���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link DynamicConstraints }
     *     
     */
    public void setDynamicConstraints(DynamicConstraints value) {
        this.dynamicConstraints = value;
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
     * ��ȡcontinuous���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContinuous() {
        return continuous;
    }

    /**
     * ����continuous���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContinuous(String value) {
        this.continuous = value;
    }

    /**
     * ��ȡdistance���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDistance() {
        return distance;
    }

    /**
     * ����distance���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDistance(String value) {
        this.distance = value;
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
     * ��ȡtimeGap���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeGap() {
        return timeGap;
    }

    /**
     * ����timeGap���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeGap(String value) {
        this.timeGap = value;
    }

}
