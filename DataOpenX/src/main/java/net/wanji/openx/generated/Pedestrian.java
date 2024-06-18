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
 * <p>Pedestrian enity
 * 
 *
 * 
 * <pre>
 * complexType name="Pedestrian">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="ParameterDeclarations" type="{}ParameterDeclarations" minOccurs="0"/>
 *         element name="BoundingBox" type="{}BoundingBox"/>
 *         element name="Properties" type="{}Properties"/>
 *       /all>
 *       attribute name="mass" use="required" type="{}Double" />
 *       attribute name="model" use="required" type="{}String" />
 *       attribute name="name" use="required" type="{}String" />
 *       attribute name="pedestrianCategory" use="required" type="{}PedestrianCategory" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pedestrian", propOrder = {

})
public class Pedestrian {

    @XmlElement(name = "ParameterDeclarations")
    protected ParameterDeclarations parameterDeclarations;
    @XmlElement(name = "BoundingBox", required = true)
    protected BoundingBox boundingBox;
    @XmlElement(name = "Properties", required = true)
    protected Properties properties;
    @XmlAttribute(name = "mass", required = true)
    protected String mass;
    @XmlAttribute(name = "model", required = true)
    protected String model;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "pedestrianCategory", required = true)
    protected String pedestrianCategory;

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
     * ��ȡmodel���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * ����model���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
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

    /**
     * ��ȡpedestrianCategory���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPedestrianCategory() {
        return pedestrianCategory;
    }

    /**
     * ����pedestrianCategory���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPedestrianCategory(String value) {
        this.pedestrianCategory = value;
    }

}
