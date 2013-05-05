package com.dubture.composer.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.dubture.composer.core.log.Logger;
import com.dubture.getcomposer.core.ComposerPackage;
import com.dubture.getcomposer.core.objects.Autoload;

public class EclipsePHPPackage implements
        NamespaceResolverInterface, InstallableItem
{
    private final ComposerPackage phpPackage;

    private IPath path;
    
    public EclipsePHPPackage(ComposerPackage phpPackage) {
        
        Assert.isNotNull(phpPackage);
        this.phpPackage = phpPackage;
    }

    @Override
    public IPath resolve(IResource resource)
    {
        Autoload autoload = phpPackage.getAutoload();
        
        if (autoload == null || autoload.getPsr0() == null || autoload.getPsr0().getFirst() == null) {
            Logger.debug("Unable to resolve namespace without autoload information " + phpPackage.getName());
            return null;
        }
         
        String targetDir = phpPackage.getTargetDir();
        IPath ns = null;
        IPath path = resource.getFullPath();
        IPath composerPath = getPath();
        
        
        IPath psr0Path = composerPath.append(autoload.getPsr0().getFirst().getNamespace());
        int segments = psr0Path.segmentCount();
         
        if (path.matchingFirstSegments(psr0Path) == segments) {
             
            if (targetDir != null && targetDir.length() > 0) {
                Path target = new Path(targetDir);
                ns = target.append(path.removeFirstSegments(psr0Path.segmentCount()));    
            } else {
                ns = path.removeFirstSegments(psr0Path.segmentCount());
            }
             
        }

        return ns;        
    }

    @Override
    public String getName()
    {
        return phpPackage.getName();
    }

    @Override
    public String getDescription()
    {
        return phpPackage.getDescription();
    }

    @Override
    public String getUrl()
    {
        return phpPackage.getHomepage();
    }

    public void setFullPath(String fullPath)
    {
        path = new Path(fullPath);
    }

    public IPath getPath()
    {
        return path;
    }

    public ComposerPackage getPhpPackage()
    {
        return phpPackage;
    }

	@Override
	public IPath reverseResolve(IProject project, String namespace) {
		// TODO Auto-generated method stub
		return null;
	}
}
