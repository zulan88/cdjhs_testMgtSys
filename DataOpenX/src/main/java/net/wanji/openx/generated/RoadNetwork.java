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
 * <p>RoadNetwork enity
 * 
 * 
 * 
 * <pre>
 * complexType name="RoadNetwork">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="LogicFile" type="{}File" minOccurs="0"/>
 *         element name="SceneGraphFile" type="{}File" minOccurs="0"/>
 *         element name="TrafficSignals" type="{}TrafficSignals" minOccurs="0"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoadNetwork", propOrder = {
    "logicFile",
    "sceneGraphFile",
    "trafficSignals"
})
public class RoadNetwork {

    @XmlElement(name = "LogicFile")
    protected File logicFile;
    @XmlElement(name = "SceneGraphFile")
    protected File sceneGraphFile;
    @XmlElement(name = "TrafficSignals")
    protected TrafficSignals trafficSignals;

    /**
     * ��ȡlogicFile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link File }
     *     
     */
    public File getLogicFile() {
        return logicFile;
    }

    /**
     * ����logicFile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link File }
     *     
     */
    public void setLogicFile(File value) {
        this.logicFile = value;
    }

    /**
     * ��ȡsceneGraphFile���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link File }
     *     
     */
    public File getSceneGraphFile() {
        return sceneGraphFile;
    }

    /**
     * ����sceneGraphFile���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link File }
     *     
     */
    public void setSceneGraphFile(File value) {
        this.sceneGraphFile = value;
    }

    /**
     * ��ȡtrafficSignals���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSignals }
     *     
     */
    public TrafficSignals getTrafficSignals() {
        return trafficSignals;
    }

    /**
     * ����trafficSignals���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSignals }
     *     
     */
    public void setTrafficSignals(TrafficSignals value) {
        this.trafficSignals = value;
    }

}
