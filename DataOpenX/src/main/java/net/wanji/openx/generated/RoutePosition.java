//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RoutePosition enity
 * 
 *
 * 
 * <pre>
 * complexType name="RoutePosition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="RouteRef" type="{}RouteRef"/>
 *         element name="Orientation" type="{}Orientation" minOccurs="0"/>
 *         element name="InRoutePosition" type="{}InRoutePosition"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoutePosition", propOrder = {

})
public class RoutePosition {

    @XmlElement(name = "RouteRef", required = true)
    protected RouteRef routeRef;
    @XmlElement(name = "Orientation")
    protected Orientation orientation;
    @XmlElement(name = "InRoutePosition", required = true)
    protected InRoutePosition inRoutePosition;

    /**
     * ��ȡrouteRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RouteRef }
     *     
     */
    public RouteRef getRouteRef() {
        return routeRef;
    }

    /**
     * ����routeRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RouteRef }
     *     
     */
    public void setRouteRef(RouteRef value) {
        this.routeRef = value;
    }

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
     * ��ȡinRoutePosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link InRoutePosition }
     *     
     */
    public InRoutePosition getInRoutePosition() {
        return inRoutePosition;
    }

    /**
     * ����inRoutePosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link InRoutePosition }
     *     
     */
    public void setInRoutePosition(InRoutePosition value) {
        this.inRoutePosition = value;
    }

}
