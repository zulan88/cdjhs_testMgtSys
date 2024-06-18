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
 * <p>MiscObject enity
 * 
 * 
 * 
 * <pre>
 * complexType name="MiscObject">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="ParameterDeclarations" type="{}ParameterDeclarations" minOccurs="0"/>
 *         element name="BoundingBox" type="{}BoundingBox"/>
 *         element name="Properties" type="{}Properties"/>
 *       /all>
 *       attribute name="mass" use="required" type="{}Double" />
 *       attribute name="miscObjectCategory" use="required" type="{}MiscObjectCategory" />
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MiscObject", propOrder = {

})
public class MiscObject {

    @XmlElement(name = "ParameterDeclarations")
    protected ParameterDeclarations parameterDeclarations;
    @XmlElement(name = "BoundingBox", required = true)
    protected BoundingBox boundingBox;
    @XmlElement(name = "Properties", required = true)
    protected Properties properties;
    @XmlAttribute(name = "mass", required = true)
    protected String mass;
    @XmlAttribute(name = "miscObjectCategory", required = true)
    protected String miscObjectCategory;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡparameterDeclarations���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ParameterDeclarations }
     *     
     */
    public ParameterDeclarations getParameterDeclarations() {
        return parameterDeclarations;
    }

    /**
     * ����parameterDeclarations���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterDeclarations }
     *     
     */
    public void setParameterDeclarations(ParameterDeclarations value) {
        this.parameterDeclarations = value;
    }

    /**
     * ��ȡboundingBox���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BoundingBox }
     *     
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * ����boundingBox���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BoundingBox }
     *     
     */
    public void setBoundingBox(BoundingBox value) {
        this.boundingBox = value;
    }

    /**
     * ��ȡproperties���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Properties }
     *     
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * ����properties���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Properties }
     *     
     */
    public void setProperties(Properties value) {
        this.properties = value;
    }

    /**
     * ��ȡmass���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMass() {
        return mass;
    }

    /**
     * ����mass���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMass(String value) {
        this.mass = value;
    }

    /**
     * ��ȡmiscObjectCategory���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiscObjectCategory() {
        return miscObjectCategory;
    }

    /**
     * ����miscObjectCategory���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiscObjectCategory(String value) {
        this.miscObjectCategory = value;
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
