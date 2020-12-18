package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntity;
import cn.xinbee.mdrs.util.JsonUtils;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

@Entity
public class MailDeliveryTemplate extends AbstractEntity<Integer> {
    private String name;
    private String content;
    private String substitutions;
    private List<String> substitutionArr;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(String substitutions) {
        this.substitutions = substitutions;
    }

    @Transient
    public List<String> getSubstitutionArr() {
        return substitutionArr;
    }

    public void setSubstitutionArr(List<String> substitutionArr) {
        this.substitutionArr = substitutionArr;
        this.setSubstitutions(JsonUtils.serialize(substitutionArr));
    }
}
