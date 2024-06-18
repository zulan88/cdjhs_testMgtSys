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
 * <p>RoutingAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="RoutingAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="AssignRouteAction" type="{}AssignRouteAction" minOccurs="0"/>
 *         element name="FollowTrajectoryAction" type="{}FollowTrajectoryAction" minOccurs="0"/>
 *         element name="AcquirePositionAction" type="{}AcquirePositionAction" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoutingAction", propOrder = {
    "assignRouteAction",
    "followTrajectoryAction",
    "acquirePositionAction"
})
public class RoutingAction {

    @XmlElement(name = "AssignRouteAction")
    protected AssignRouteAction assignRouteAction;
    @XmlElement(name = "FollowTrajectoryAction")
    protected FollowTrajectoryAction followTrajectoryAction;
    @XmlElement(name = "AcquirePositionAction")
    protected AcquirePositionAction acquirePositionAction;

    /**
     * ��ȡassignRouteAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AssignRouteAction }
     *     
     */
    public AssignRouteAction getAssignRouteAction() {
        return assignRouteAction;
    }

    /**
     * ����assignRouteAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AssignRouteAction }
     *     
     */
    public void setAssignRouteAction(AssignRouteAction value) {
        this.assignRouteAction = value;
    }

    /**
     * ��ȡfollowTrajectoryAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link FollowTrajectoryAction }
     *     
     */
    public FollowTrajectoryAction getFollowTrajectoryAction() {
        return followTrajectoryAction;
    }

    /**
     * ����followTrajectoryAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link FollowTrajectoryAction }
     *     
     */
    public void setFollowTrajectoryAction(FollowTrajectoryAction value) {
        this.followTrajectoryAction = value;
    }

    /**
     * ��ȡacquirePositionAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AcquirePositionAction }
     *     
     */
    public AcquirePositionAction getAcquirePositionAction() {
        return acquirePositionAction;
    }

    /**
     * ����acquirePositionAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AcquirePositionAction }
     *     
     */
    public void setAcquirePositionAction(AcquirePositionAction value) {
        this.acquirePositionAction = value;
    }

}
