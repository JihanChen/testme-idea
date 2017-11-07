package com.weirddev.testme.intellij.template;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiUtil;
import com.weirddev.testme.intellij.template.context.Type;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Date: 26/11/2016
 *
 * @author Yaron Yamin
 */
public class TypeDictionary {
    private static final Logger LOG = Logger.getInstance(TypeDictionary.class.getName());
    Map<String, Type> typeDictionary =new HashMap<String, Type>();
    private final PsiClass srcClass;
    private final PsiPackage targetPackage;
    private AtomicInteger newTypeCounter=new AtomicInteger();
    private AtomicInteger existingTypeHitsCounter=new AtomicInteger();
    public TypeDictionary(PsiClass srcClass, PsiPackage targetPackage) {
        this.srcClass = srcClass;
        this.targetPackage = targetPackage;
    }

    @Nullable
    public Type getType(PsiType psiType, int maxRecursionDepth, boolean shouldResolveAllMethods) {
        Type type = null;
        if (psiType != null) {
            final String canonicalText = psiType.getCanonicalText();
            type = typeDictionary.get(canonicalText);
            if (type == null || !type.isDependenciesResolvable() && shouldResolveAllMethods && maxRecursionDepth>1) {
                LOG.debug(newTypeCounter.incrementAndGet()+". Creating new type object for:" + canonicalText+" maxRecursionDepth:"+maxRecursionDepth);
                type = new Type(psiType, this, maxRecursionDepth,shouldResolveAllMethods);
                typeDictionary.put(canonicalText, type);
                type.resolveDependencies(this,maxRecursionDepth, psiType,shouldResolveAllMethods);
            } else {
                LOG.debug(existingTypeHitsCounter.incrementAndGet()+". Found existing type object for:" + canonicalText+" maxRecursionDepth:"+maxRecursionDepth);
            }
        }
        return type;
    }

    public boolean isAccessible(PsiMethod psiMethod) {
        return PsiUtil.isAccessibleFromPackage(psiMethod, targetPackage) && (psiMethod.getContainingClass()==null || PsiUtil.isAccessibleFromPackage(psiMethod.getContainingClass(), targetPackage));
    }
}
