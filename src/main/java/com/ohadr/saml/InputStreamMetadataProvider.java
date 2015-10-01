package com.ohadr.saml;

import java.io.InputStream;
import java.util.Timer;

import org.opensaml.saml2.metadata.provider.AbstractReloadingMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;

public class InputStreamMetadataProvider extends AbstractReloadingMetadataProvider
{
    /** The metadata stream. */
    private final InputStream metadataInputStream;

    /**
     * Constructor.
     * 
     * @param metadata the metadata stream
     * 
     * @throws MetadataProviderException thrown if the given stream is null or if the metadata can not be parsed
     */
    public InputStreamMetadataProvider(InputStream metadata) throws MetadataProviderException 
    {
        super();
        metadataInputStream = metadata;
    }

    /**
     * Constructor.
     * 
     * @param metadata the metadata stream
     * @param backgroundTaskTimer timer used to refresh metadata in the background
     * 
     * @throws MetadataProviderException thrown if the given stream is null or if the metadata can not be parsed
     */
    public InputStreamMetadataProvider(Timer backgroundTaskTimer, InputStream metadata) throws MetadataProviderException 
    {
        super(backgroundTaskTimer);
        metadataInputStream = metadata;
    }

	@Override
	protected String getMetadataIdentifier()
	{
		return toString();
	}

	@Override
	protected byte[] fetchMetadata() throws MetadataProviderException
	{
       	return inputstreamToByteArray( metadataInputStream );
	}

}
