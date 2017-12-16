package com.weirddev.testme.intellij.template.context;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiParameter;
import com.weirddev.testme.intellij.resolvers.groovy.LanguageUtils;
import com.weirddev.testme.intellij.scala.resolvers.ScalaPsiTreeUtils;
import com.weirddev.testme.intellij.template.TypeDictionary;

import java.util.ArrayList;

/**
 * Date: 24/10/2016
 * @author Yaron Yamin
 */
public class Param {
    private final Type type;
    private String name;
    private final ArrayList<Field> assignedToFields;

    public Param(PsiParameter psiParameter, TypeDictionary typeDictionary, int maxRecursionDepth, ArrayList<Field> assignedToFields) {
        this(resolveType(psiParameter, typeDictionary, maxRecursionDepth), psiParameter.getName(),assignedToFields);
    }

    private static Type resolveType(PsiParameter psiParameter, TypeDictionary typeDictionary, int maxRecursionDepth) {

        PsiElement psiElement = null;
        if (LanguageUtils.isScala(psiParameter.getLanguage())) {
            psiElement = ScalaPsiTreeUtils.resolveRelatedTypeElement(psiParameter);
        }
        return typeDictionary.getType(psiParameter.getType(), maxRecursionDepth, false,psiElement);
    }

    public Param(Type type, String name, ArrayList<Field> assignedToFields) {
        this.type = type;
        this.name = name;
        this.assignedToFields = assignedToFields;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Field> getAssignedToFields() {
        return assignedToFields;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Param)) return false;

        Param param = (Param) o;

        return type != null ? type.equals(param.type) : param.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Param{" + "name='" + name + ", type=" + type + '\'' + ", assignedToFields=" + assignedToFields + '}';
    }
}
