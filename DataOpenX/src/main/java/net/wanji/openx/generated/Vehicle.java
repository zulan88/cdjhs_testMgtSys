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
 * <p>Vehicle enity
 *
 *
 *
 * <pre>
 * complexType name="Vehicle">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="ParameterDeclarations" type="{}ParameterDeclarations" minOccurs="0"/>
 *         element name="BoundingBox" type="{}BoundingBox"/>
 *         element name="Performance" type="{}Performance"/>
 *         element name="Axles" type="{}Axles"/>
 *         element name="Properties" type="{}Properties"/>
 *       /all>
 *       attribute name="name" use="required" type="{}String" />
 *       attribute name="vehicleCategory" use="required" type="{}VehicleCategory" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vehicle", propOrder = {

})
public class Vehicle {

    public Vehicle(String type, String stand) {
        this.boundingBox = new BoundingBox(type);
        this.performance = new Performance(type);
        this.axles = new Axles(type);
        this.name = type;
        this.vehicleCategory = stand + "car";

    }

    public Vehicle() {
    }

    @XmlElement(name = "ParameterDeclarations")
    protected ParameterDeclarations parameterDeclarations;
    @XmlElement(name = "BoundingBox", required = true)
    protected BoundingBox boundingBox;
    @XmlElement(name = "Performance", required = true)
    protected Performance performance;
    @XmlElement(name = "Axles", required = true)
    protected Axles axles;
    @XmlElement(name = "Properties", required = true)
    protected Properties properties;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "vehicleCategory", required = true)
    protected String vehicleCategory;

    /**
     * ��ȡparameterDeclarations���Ե�ֵ��
     *
     * @return possible object is
     * {@link ParameterDeclarations }
     */
    public ParameterDeclarations getParameterDeclarations() {
        return parameterDeclarations;
    }

    /**
     * ����parameterDeclarations���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link ParameterDeclarations }
     */
    public void setParameterDeclarations(ParameterDeclarations value) {
        this.parameterDeclarations = value;
    }

    /**
     * ��ȡboundingBox���Ե�ֵ��
     *
     * @return possible object is
     * {@link BoundingBox }
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     * ����boundingBox���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link BoundingBox }
     */
    public void setBoundingBox(BoundingBox value) {
        this.boundingBox = value;
    }

    /**
     * ��ȡperformance���Ե�ֵ��
     *
     * @return possible object is
     * {@link Performance }
     */
    public Performance getPerformance() {
        return performance;
    }

    /**
     * ����performance���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link Performance }
     */
    public void setPerformance(Performance value) {
        this.performance = value;
    }

    /**
     * ��ȡaxles���Ե�ֵ��
     *
     * @return possible object is
     * {@link Axles }
     */
    public Axles getAxles() {
        return axles;
    }

    /**
     * ����axles���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link Axles }
     */
    public void setAxles(Axles value) {
        this.axles = value;
    }

    /**
     * ��ȡproperties���Ե�ֵ��
     *
     * @return possible object is
     * {@link Properties }
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * ����properties���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link Properties }
     */
    public void setProperties(Properties value) {
        this.properties = value;
    }

    /**
     * ��ȡname���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * ��ȡvehicleCategory���Ե�ֵ��
     *
     * @return possible object is
     * {@link String }
     */
    public String getVehicleCategory() {
        return vehicleCategory;
    }

    /**
     * ����vehicleCategory���Ե�ֵ��
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVehicleCategory(String value) {
        this.vehicleCategory = value;
    }

}
