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
 * <p>Catalog enity
 * 
 *
 * 
 * <pre>
 * complexType name="Catalog">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Vehicle" type="{}Vehicle" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Controller" type="{}Controller" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Pedestrian" type="{}Pedestrian" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="MiscObject" type="{}MiscObject" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Environment" type="{}Environment" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Maneuver" type="{}Maneuver" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Trajectory" type="{}Trajectory" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="Route" type="{}Route" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *       attribute name="name" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Catalog", propOrder = {
    "vehicle",
    "controller",
    "pedestrian",
    "miscObject",
    "environment",
    "maneuver",
    "trajectory",
    "route"
})
public class Catalog {

    @XmlElement(name = "Vehicle")
    protected List<Vehicle> vehicle;
    @XmlElement(name = "Controller")
    protected List<Controller> controller;
    @XmlElement(name = "Pedestrian")
    protected List<Pedestrian> pedestrian;
    @XmlElement(name = "MiscObject")
    protected List<MiscObject> miscObject;
    @XmlElement(name = "Environment")
    protected List<Environment> environment;
    @XmlElement(name = "Maneuver")
    protected List<Maneuver> maneuver;
    @XmlElement(name = "Trajectory")
    protected List<Trajectory> trajectory;
    @XmlElement(name = "Route")
    protected List<Route> route;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the vehicle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vehicle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVehicle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Vehicle }
     * 
     * 
     */
    public List<Vehicle> getVehicle() {
        if (vehicle == null) {
            vehicle = new ArrayList<Vehicle>();
        }
        return this.vehicle;
    }

    /**
     * Gets the value of the controller property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the controller property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getController().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Controller }
     * 
     * 
     */
    public List<Controller> getController() {
        if (controller == null) {
            controller = new ArrayList<Controller>();
        }
        return this.controller;
    }

    /**
     * Gets the value of the pedestrian property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pedestrian property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPedestrian().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pedestrian }
     * 
     * 
     */
    public List<Pedestrian> getPedestrian() {
        if (pedestrian == null) {
            pedestrian = new ArrayList<Pedestrian>();
        }
        return this.pedestrian;
    }

    /**
     * Gets the value of the miscObject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the miscObject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMiscObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MiscObject }
     * 
     * 
     */
    public List<MiscObject> getMiscObject() {
        if (miscObject == null) {
            miscObject = new ArrayList<MiscObject>();
        }
        return this.miscObject;
    }

    /**
     * Gets the value of the environment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the environment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvironment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Environment }
     * 
     * 
     */
    public List<Environment> getEnvironment() {
        if (environment == null) {
            environment = new ArrayList<Environment>();
        }
        return this.environment;
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
     * Gets the value of the trajectory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trajectory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrajectory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Trajectory }
     * 
     * 
     */
    public List<Trajectory> getTrajectory() {
        if (trajectory == null) {
            trajectory = new ArrayList<Trajectory>();
        }
        return this.trajectory;
    }

    /**
     * Gets the value of the route property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the route property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Route }
     * 
     * 
     */
    public List<Route> getRoute() {
        if (route == null) {
            route = new ArrayList<Route>();
        }
        return this.route;
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
