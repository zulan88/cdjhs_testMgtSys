//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ManeuverGroup enity
 * 
 *
 * 
 * <pre>
 * complexType name="ManeuverGroup">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Actors" type="{}Actors"/>
 *         element name="CatalogReference" type="{}CatalogReference" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Maneuver" type="{}Maneuver" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *       attribute name="maximumExecutionCount" use="required" type="{}UnsignedInt" />
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManeuverGroup", propOrder = {
    "actors",
    "catalogReference",
    "maneuver"
})
public class ManeuverGroup {

    @XmlElement(name = "Actors", required = true)
    protected Actors actors;
    @XmlElement(name = "CatalogReference")
    protected List<CatalogReference> catalogReference;
    @XmlElement(name = "Maneuver")
    protected List<Maneuver> maneuver;
    @XmlAttribute(name = "maximumExecutionCount", required = true)
    protected String maximumExecutionCount;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡactors���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Actors }
     *     
     */
    public Actors getActors() {
        return actors;
    }

    /**
     * ����actors���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Actors }
     *     
     */
    public void setActors(Actors value) {
        this.actors = value;
    }

    /**
     * Gets the value of the catalogReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the catalogReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCatalogReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CatalogReference }
     * 
     * 
     */
    public List<CatalogReference> getCatalogReference() {
        if (catalogReference == null) {
            catalogReference = new ArrayList<CatalogReference>();
        }
        return this.catalogReference;
    }

    /**
     * Gets the value of the maneuver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the maneuver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManeuver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Maneuver }
     * 
     * 
     */
    public List<Maneuver> getManeuver() {
        if (maneuver == null) {
            maneuver = new ArrayList<Maneuver>();
        }
        return this.maneuver;
    }

    /**
     * ��ȡmaximumExecutionCount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumExecutionCount() {
        return maximumExecutionCount;
    }

    /**
     * ����maximumExecutionCount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumExecutionCount(String value) {
        this.maximumExecutionCount = value;
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
