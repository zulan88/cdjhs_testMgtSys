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
 * <p>ScenarioObject enity
 * 
 * 
 * 
 * <pre>
 * complexType name="ScenarioObject">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         group ref="{}EntityObject"/>
 *         element name="ObjectController" type="{}ObjectController" minOccurs="0"/>
 *       /sequence>
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScenarioObject", propOrder = {
    "catalogReference",
    "vehicle",
    "pedestrian",
    "miscObject",
    "objectController"
})
public class ScenarioObject {

    @XmlElement(name = "CatalogReference")
    protected CatalogReference catalogReference;
    @XmlElement(name = "Vehicle")
    protected Vehicle vehicle;
    @XmlElement(name = "Pedestrian")
    protected Pedestrian pedestrian;
    @XmlElement(name = "MiscObject")
    protected MiscObject miscObject;
    @XmlElement(name = "ObjectController")
    protected ObjectController objectController;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡcatalogReference���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CatalogReference }
     *     
     */
    public CatalogReference getCatalogReference() {
        return catalogReference;
    }

    /**
     * ����catalogReference���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CatalogReference }
     *     
     */
    public void setCatalogReference(CatalogReference value) {
        this.catalogReference = value;
    }

    /**
     * ��ȡvehicle���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Vehicle }
     *     
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * ����vehicle���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Vehicle }
     *     
     */
    public void setVehicle(Vehicle value) {
        this.vehicle = value;
    }

    /**
     * ��ȡpedestrian���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Pedestrian }
     *     
     */
    public Pedestrian getPedestrian() {
        return pedestrian;
    }

    /**
     * ����pedestrian���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Pedestrian }
     *     
     */
    public void setPedestrian(Pedestrian value) {
        this.pedestrian = value;
    }

    /**
     * ��ȡmiscObject���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link MiscObject }
     *     
     */
    public MiscObject getMiscObject() {
        return miscObject;
    }

    /**
     * ����miscObject���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link MiscObject }
     *     
     */
    public void setMiscObject(MiscObject value) {
        this.miscObject = value;
    }

    /**
     * ��ȡobjectController���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ObjectController }
     *     
     */
    public ObjectController getObjectController() {
        return objectController;
    }

    /**
     * ����objectController���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ObjectController }
     *     
     */
    public void setObjectController(ObjectController value) {
        this.objectController = value;
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
