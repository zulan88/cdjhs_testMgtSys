//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.*;


/**
 * <p>OpenScenario enity
 * 
 *
 * 
 * <pre>
 * complexType name="OpenScenario">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="FileHeader" type="{}FileHeader"/>
 *         group ref="{}OpenScenarioCategory"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OpenScenario", propOrder = {
    "fileHeader",
    "parameterDeclarations",
    "catalogLocations",
    "roadNetwork",
    "entities",
    "storyboard",
    "catalog"
})
@XmlRootElement(name = "OpenSCENARIO")
public class OpenScenario {

    @XmlElement(name = "FileHeader", required = true)
    protected FileHeader fileHeader;
    @XmlElement(name = "ParameterDeclarations")
    protected ParameterDeclarations parameterDeclarations;
    @XmlElement(name = "CatalogLocations")
    protected CatalogLocations catalogLocations;
    @XmlElement(name = "RoadNetwork")
    protected RoadNetwork roadNetwork;
    @XmlElement(name = "Entities")
    protected Entities entities;
    @XmlElement(name = "Storyboard")
    protected Storyboard storyboard;
    @XmlElement(name = "Catalog")
    protected Catalog catalog;

    /**
     * ��ȡfileHeader���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link FileHeader }
     *     
     */
    public FileHeader getFileHeader() {
        return fileHeader;
    }

    /**
     * ����fileHeader���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link FileHeader }
     *     
     */
    public void setFileHeader(FileHeader value) {
        this.fileHeader = value;
    }

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
     * ��ȡcatalogLocations���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CatalogLocations }
     *     
     */
    public CatalogLocations getCatalogLocations() {
        return catalogLocations;
    }

    /**
     * ����catalogLocations���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CatalogLocations }
     *     
     */
    public void setCatalogLocations(CatalogLocations value) {
        this.catalogLocations = value;
    }

    /**
     * ��ȡroadNetwork���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RoadNetwork }
     *     
     */
    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    /**
     * ����roadNetwork���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RoadNetwork }
     *     
     */
    public void setRoadNetwork(RoadNetwork value) {
        this.roadNetwork = value;
    }

    /**
     * ��ȡentities���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Entities }
     *     
     */
    public Entities getEntities() {
        return entities;
    }

    /**
     * ����entities���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Entities }
     *     
     */
    public void setEntities(Entities value) {
        this.entities = value;
    }

    /**
     * ��ȡstoryboard���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Storyboard }
     *     
     */
    public Storyboard getStoryboard() {
        return storyboard;
    }

    /**
     * ����storyboard���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Storyboard }
     *     
     */
    public void setStoryboard(Storyboard value) {
        this.storyboard = value;
    }

    /**
     * ��ȡcatalog���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Catalog }
     *     
     */
    public Catalog getCatalog() {
        return catalog;
    }

    /**
     * ����catalog���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Catalog }
     *     
     */
    public void setCatalog(Catalog value) {
        this.catalog = value;
    }

}
