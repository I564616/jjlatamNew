/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.util;

import de.hybris.platform.util.Config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPCompressedData;
import org.bouncycastle.openpgp.PGPCompressedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPEncryptedDataGenerator;
import org.bouncycastle.openpgp.PGPEncryptedDataList;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyEncryptedData;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPUtil;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Encryption;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;



/**
 * Implementation of the Encryption Service.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjEncryptionUtil
{

	/** The Constant RSA_ALGORITHM. */
	private static final String RSA_ALGORITHM = "RSA";

	/** The Constant DECRYPTED_FILE_KEYWORD. */
	private static final String DECRYPTED_FILE_KEYWORD = "_dec";

	/** The Constant ENCRPYTED_FLIE_KEYWORD. */
	private static final String ENCRPYTED_FILE_KEYWORD = "_enc";

	/** The Constant FIND_PRIVATE_KEY. */
	private static final String FIND_PRIVATE_KEY = "findPrivateKey()";

	/** The Constant DECRYPT_DATA. */
	private static final String DECRYPT_DATA = "decryptData()";

	/** The Constant READ_PUBLIC_KEY. */
	private static final String READ_PUBLIC_KEY = "readPublicKey()";

	/** The Constant ENCRYPT_DATA. */
	private static final String ENCRYPT_DATA = "encryptData()";

	/** The Constant EXPORT_KEY_PAIR. */
	private static final String EXPORT_KEY_PAIR = "exportKeyPair()";

	/** The Constant GENERATE_PGP_KEY_PAIRS. */
	private static final String GENERATE_PGP_KEY_PAIRS = "generatePGPKeyPairs()";

	/** The Constant BOUNCY_CASTLE_PROVIDER. */
	private static final String BOUNCY_CASTLE_PROVIDER = "BC";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjEncryptionUtil.class);

	/** The Constant DEFAULT_ENCRYPTION_BITS. */
	private static final int DEFAULT_ENCRYPTION_BITS = 2048;

	/**
	 * Generate PGP key pairs.
	 *
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 */
	public static boolean generatePGPKeyPairs() throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		boolean keysGenerated = true;
		final String rootFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT);
		final String incomingFolderPath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_INCOMING);
		final String encryptionFolderName = Config.getParameter(Encryption.ENCRYPTION_FODLER_NAME);

		if (StringUtils.isEmpty(rootFilePath) || StringUtils.isEmpty(incomingFolderPath)
				|| StringUtils.isEmpty(encryptionFolderName))
		{
			throw new BusinessException("Unable to find Folder path for Encryption Folder.");
		}

		final String encryptionFolderPath = rootFilePath + incomingFolderPath + encryptionFolderName;
		LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
				+ "Encryption Folder path is  [ " + encryptionFolderPath + " ].");

		final File encryptionFolder = new File(FilenameUtils.getFullPath(encryptionFolderPath),
				FilenameUtils.getName(encryptionFolderPath));
		if (!encryptionFolder.exists())
		{
			LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Encryption Folder does not exists. Creating the folder.");
			if (!encryptionFolder.mkdirs())
			{
				throw new BusinessException("Unable to create folder at path [" + encryptionFolderPath + "].");
			}
			else
			{
				LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
						+ "Successfully created Encryption Folder.");
			}
		}

		final String privateKeyName = Config.getParameter(Encryption.ENCRYPTION_PRIVATE_KEY_NAME);
		final String publicKeyName = Config.getParameter(Encryption.ENCRYPTION_PUBLIC_KEY_NAME);
		final String userId = Config.getParameter(Encryption.ENCRYPTION_USER_ID);
		final String password = Config.getParameter(Encryption.ENCRYPTION_PASSWORD);
		final int encryptionBits = Config.getInt(Encryption.ENCRYPTION_BITS_VALUE, DEFAULT_ENCRYPTION_BITS);
		final boolean isArmored = Boolean.valueOf(Config.getParameter(Encryption.ENCRYPTION_ARMORED)).booleanValue();
		FileOutputStream privateKeyOutputStream = null;
		FileOutputStream publicKeyOutputStream = null;
		try
		{
			Security.addProvider(new BouncyCastleProvider());
			final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
			keyPairGenerator.initialize(encryptionBits);
			final KeyPair keyPair = keyPairGenerator.generateKeyPair();

			final String privateKeyPath = encryptionFolderPath + privateKeyName + Encryption.KEY_FILE_EXTENSION_ASC;
			final String publicKeyPath = encryptionFolderPath + publicKeyName + Encryption.KEY_FILE_EXTENSION_ASC;

			LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Genrating Private Key at Location [ " + privateKeyPath + " ] for User Id [ " + userId + " ]");
			LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Genrating Public Key at Location [ " + publicKeyPath + " ] for User Id [ " + userId + " ]");

			privateKeyOutputStream = new FileOutputStream(
					new File(encryptionFolderPath, FilenameUtils.getName(privateKeyName + Encryption.KEY_FILE_EXTENSION_ASC)));
			publicKeyOutputStream = new FileOutputStream(
					new File(encryptionFolderPath, FilenameUtils.getName(publicKeyName + Encryption.KEY_FILE_EXTENSION_ASC)));

			exportKeyPair(privateKeyOutputStream, publicKeyOutputStream, keyPair.getPublic(), keyPair.getPrivate(), userId,
					password.toCharArray(), isArmored);
		}
		catch (final NoSuchAlgorithmException noSuchAlgorithmException)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Unable to genrate PGP Keys at the localtion [" + encryptionFolderPath + "]");
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "NoSuchAlgorithmException Occured.", noSuchAlgorithmException);
			keysGenerated = false;
			throw new BusinessException("NoSuchAlgorithmException Occured. Terminating the Job.");
		}
		catch (final FileNotFoundException fileNotFoundException)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Unable to genrate PGP Keys at the localtion [" + encryptionFolderPath + "]");
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "FileNotFoundException Occured.", fileNotFoundException);
			keysGenerated = false;
			throw new BusinessException("FileNotFoundException Occured. Terminating the Job.");
		}
		catch (final PGPException pGPException)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Unable to genrate PGP Keys at the localtion [" + encryptionFolderPath + "]");
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN + "PGPException Occured.",
					pGPException);
			keysGenerated = false;
			throw new BusinessException("PGPException Occured. Terminating the Job.");
		}
		catch (final IOException iOException)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Unable to genrate PGP Keys at the localtion [" + encryptionFolderPath + "]");
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN + "IOException Occured.",
					iOException);
			keysGenerated = false;
			throw new BusinessException("IOException Occured. Terminating the Job.");
		}
		catch (final NoSuchProviderException noSuchProviderException)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Unable to genrate PGP Keys at the localtion [" + encryptionFolderPath + "]");
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "NoSuchProviderException Occured.", noSuchProviderException);
			keysGenerated = false;
			throw new BusinessException("NoSuchProviderException Occured. Terminating the Job.");
		}
		finally
		{
			try
			{
				if (privateKeyOutputStream != null)
				{
					privateKeyOutputStream.close();
				}
			}
			catch (final IOException e)
			{
				LOGGER.error("Issue closing Output steram" + e);
			}

			finally
			{

				try
				{
					if (publicKeyOutputStream != null)
					{
						publicKeyOutputStream.close();
					}
				}
				catch (final IOException e)
				{
					LOGGER.error("Issue closing Output steram" + e);
				}
			}
		}

		if (keysGenerated && LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN
					+ "Successfully Gerneated PGP Keys at the localtion [" + encryptionFolderPath + "]");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + GENERATE_PGP_KEY_PAIRS + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return keysGenerated;
	}

	/**
	 * Export key pair.
	 *
	 * @param privateKeyOutputStream
	 *           the private key output stream
	 * @param publicKeyOutputStream
	 *           the public key output stream
	 * @param publicKey
	 *           the public key
	 * @param privateKey
	 *           the private key
	 * @param userId
	 *           the user id
	 * @param passwordPhrase
	 *           the password phrase
	 * @param isArmored
	 *           the is armored
	 * @throws PGPException
	 *            the pGP exception
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws NoSuchProviderException
	 *            the no such provider exception
	 */
	private static void exportKeyPair(final OutputStream privateKeyOutputStream, final OutputStream publicKeyOutputStream,
			final PublicKey publicKey, final PrivateKey privateKey, final String userId, final char[] passwordPhrase,
			final boolean isArmored) throws PGPException, IOException, NoSuchProviderException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + EXPORT_KEY_PAIR + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		if (isArmored)
		{
			new ArmoredOutputStream(privateKeyOutputStream);
		}
		final PGPSecretKey pGPSecretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, PGPPublicKey.RSA_GENERAL, publicKey,
				privateKey, new Date(), userId, PGPEncryptedData.CAST5, passwordPhrase, null, null, new SecureRandom(),
				BOUNCY_CASTLE_PROVIDER);
		pGPSecretKey.encode(privateKeyOutputStream);

		if (isArmored)
		{
			new ArmoredOutputStream(publicKeyOutputStream);
		}
		final PGPPublicKey key = pGPSecretKey.getPublicKey();
		key.encode(publicKeyOutputStream);


		final long publicKeyId = pGPSecretKey.getPublicKey().getKeyID();
		final long privateKeyId = pGPSecretKey.getKeyID();

		LOGGER.info(
				Logging.JNJ_ENCYPTION + Logging.HYPHEN + EXPORT_KEY_PAIR + Logging.HYPHEN + "Public Key ID: [" + publicKeyId + "]");
		LOGGER.info(
				Logging.JNJ_ENCYPTION + Logging.HYPHEN + EXPORT_KEY_PAIR + Logging.HYPHEN + "Private Key ID: [" + privateKeyId + "]");

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + EXPORT_KEY_PAIR + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Encrypt data.
	 *
	 * @param publicKeyPath
	 *           the public key path
	 * @param fileToEncrypt
	 *           the file to encrypt
	 * @param isArmored
	 *           the is armored
	 * @param withIntegrityCheck
	 *           the with integrity check
	 * @return the file
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws PGPException
	 *            the pGP exception
	 * @throws NoSuchProviderException
	 *            the no such provider exception
	 */
	public static File encryptFile(final String publicKeyPath, final File fileToEncrypt, final boolean isArmored,
			final boolean withIntegrityCheck) throws IOException, PGPException, NoSuchProviderException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + ENCRYPT_DATA + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		final PGPPublicKey pGPPublicKey = readPublicKey(publicKeyPath);
		final File cipheredFile = new File(FilenameUtils.getFullPath(fileToEncrypt.getAbsolutePath()),
				FilenameUtils.getBaseName(fileToEncrypt.getAbsolutePath()) + ENCRPYTED_FILE_KEYWORD + Jnjb2bCoreConstants.CONST_DOT
						+ FilenameUtils.getExtension(fileToEncrypt.getAbsolutePath()));
		OutputStream cipheredFileOs = new FileOutputStream(cipheredFile);
		Security.addProvider(new BouncyCastleProvider());
		if (isArmored)
		{
			cipheredFileOs = new ArmoredOutputStream(cipheredFileOs);
		}
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final PGPCompressedDataGenerator pGPCompressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);

		PGPUtil.writeFileToLiteralData(pGPCompressedDataGenerator.open(byteArrayOutputStream), PGPLiteralData.BINARY,
				fileToEncrypt);
		pGPCompressedDataGenerator.close();

		final PGPEncryptedDataGenerator pGPEncryptedDataGenerator = new PGPEncryptedDataGenerator(PGPEncryptedData.CAST5,
				withIntegrityCheck, new SecureRandom(), BOUNCY_CASTLE_PROVIDER);

		pGPEncryptedDataGenerator.addMethod(pGPPublicKey);

		final byte[] bytes = byteArrayOutputStream.toByteArray();

		final OutputStream outputStream = pGPEncryptedDataGenerator.open(cipheredFileOs, bytes.length);
		outputStream.write(bytes);
		outputStream.close();
		cipheredFileOs.close();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + ENCRYPT_DATA + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return cipheredFile;
	}

	/**
	 * This method reads the public key from the path specified.
	 *
	 * @param publicKeyPath
	 *           the public key path
	 * @return the pGP public key
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws PGPException
	 *            the pGP exception
	 */
	private static PGPPublicKey readPublicKey(final String publicKeyPath) throws IOException, PGPException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + READ_PUBLIC_KEY + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		final InputStream publicKeyInputStream = PGPUtil.getDecoderStream(
				new FileInputStream(FilenameUtils.getFullPath(publicKeyPath) + FilenameUtils.getName(publicKeyPath)));
		final PGPPublicKeyRingCollection pgpPub = new PGPPublicKeyRingCollection(publicKeyInputStream);
		PGPPublicKey finalPGPPublicKey = null;

		/* we just loop through the collection till we find a key suitable for encryption */
		final Iterator<PGPPublicKeyRing> keyRingIterator = pgpPub.getKeyRings();
		/* Iterate through keyRings */
		while (finalPGPPublicKey == null && keyRingIterator.hasNext())
		{
			final PGPPublicKeyRing pGPPublicKeyRing = keyRingIterator.next();
			final Iterator<PGPPublicKey> pGPPublicKeyIterator = pGPPublicKeyRing.getPublicKeys();
			/* Iterate through Public Keys */
			while (finalPGPPublicKey == null && pGPPublicKeyIterator.hasNext())
			{
				final PGPPublicKey pGPPublicKey = pGPPublicKeyIterator.next();
				/* Check if the public key is the EncryptionKey */
				if (pGPPublicKey.isEncryptionKey())
				{
					finalPGPPublicKey = pGPPublicKey;
				}
			}
		}
		if (finalPGPPublicKey == null)
		{
			throw new PGPException(Logging.JNJ_ENCYPTION + Logging.HYPHEN + READ_PUBLIC_KEY + Logging.HYPHEN
					+ "Can't find encryption key in key ring.");
		}
		publicKeyInputStream.close();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + READ_PUBLIC_KEY + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return finalPGPPublicKey;
	}


	/**
	 * DEECRYPT file.
	 *
	 * @param encryptedFilePath
	 *           the encrypted file path
	 * @param privateKeyFilePath
	 *           the private key file path
	 * @param password
	 *           the password
	 * @return the file
	 * @throws PGPException
	 *            the pGP exception
	 */
	public static File decryptFile(final String encryptedFilePath, final String privateKeyFilePath, final String password)
			throws PGPException

	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		Security.addProvider(new BouncyCastleProvider());
		InputStream encryptedFileIs = null;
		File decryptedFile = null;
		InputStream privKeyIn = null;
		FileOutputStream decryptedFileOs = null;
		try
		{
			encryptedFileIs = PGPUtil.getDecoderStream(
					new FileInputStream(FilenameUtils.getFullPath(encryptedFilePath) + FilenameUtils.getName(encryptedFilePath)));
			privKeyIn = PGPUtil.getDecoderStream(
					new FileInputStream(FilenameUtils.getFullPath(privateKeyFilePath) + FilenameUtils.getName(privateKeyFilePath)));

			String decrpyptedFilePath = null;
			if (StringUtils.contains(encryptedFilePath, ENCRPYTED_FILE_KEYWORD))
			{
				decrpyptedFilePath = FilenameUtils.getFullPath(encryptedFilePath)
						+ FilenameUtils.getBaseName(encryptedFilePath).substring(0,
								FilenameUtils.getBaseName(encryptedFilePath).lastIndexOf(ENCRPYTED_FILE_KEYWORD))
						+ DECRYPTED_FILE_KEYWORD + Jnjb2bCoreConstants.CONST_DOT + FilenameUtils.getExtension(encryptedFilePath);
			}
			else
			{
				decrpyptedFilePath = FilenameUtils.getFullPath(encryptedFilePath) + FilenameUtils.getBaseName(encryptedFilePath)
						+ DECRYPTED_FILE_KEYWORD + Jnjb2bCoreConstants.CONST_DOT + FilenameUtils.getExtension(encryptedFilePath);
			}
			decryptedFile = new File(FilenameUtils.getFullPath(decrpyptedFilePath), FilenameUtils.getName(decrpyptedFilePath));
			decryptedFileOs = new FileOutputStream(decryptedFile);

			/* Getting the PrivateKey */
			final PGPObjectFactory pGPObjectFactory = new PGPObjectFactory(encryptedFileIs);
			PGPEncryptedDataList pGPEncryptedDataList = null;
			final Object object = pGPObjectFactory.nextObject();
			/* the first object might be a PGP marker packet. */
			if (object instanceof PGPEncryptedDataList)
			{
				pGPEncryptedDataList = (PGPEncryptedDataList) object;
			}
			else
			{
				pGPEncryptedDataList = (PGPEncryptedDataList) pGPObjectFactory.nextObject();
			}

			/* find the secret key */
			final Iterator<PGPPublicKeyEncryptedData> pGPPublicKeyEncryptedDataItertator = pGPEncryptedDataList
					.getEncryptedDataObjects();
			PGPPrivateKey pGPPrivateKey = null;
			PGPPublicKeyEncryptedData pGPPublicKeyEncryptedData = null;

			final PGPSecretKeyRingCollection pGPSecretKeyRingCollection = new PGPSecretKeyRingCollection(
					PGPUtil.getDecoderStream(privKeyIn));

			while (pGPPrivateKey == null && pGPPublicKeyEncryptedDataItertator.hasNext())
			{
				pGPPublicKeyEncryptedData = pGPPublicKeyEncryptedDataItertator.next();
				try
				{
					pGPPrivateKey = findPrivateKey(pGPSecretKeyRingCollection, pGPPublicKeyEncryptedData.getKeyID(),
							password.toCharArray());
				}
				catch (final Exception exception)
				{
					LOGGER.warn(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
							+ "Unable to extract Private Key Corressoponding to Encrpted File. ", exception);
				}
			}

			if (null == pGPPrivateKey)
			{
				throw new PGPException(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
						+ "Unable to extract Private Key Corressoponding to Encrypted File.");
			}
			final InputStream clear = pGPPublicKeyEncryptedData.getDataStream(pGPPrivateKey, BOUNCY_CASTLE_PROVIDER);

			final PGPObjectFactory plainFact = new PGPObjectFactory(clear);

			Object message = plainFact.nextObject();

			if (message instanceof PGPCompressedData)
			{
				final PGPCompressedData pGPCompressedData = (PGPCompressedData) message;
				final PGPObjectFactory pGPCompressedDataObjectFactory = new PGPObjectFactory(pGPCompressedData.getDataStream());

				message = pGPCompressedDataObjectFactory.nextObject();
			}

			if (message instanceof PGPLiteralData)
			{
				final PGPLiteralData pGPLiteralData = (PGPLiteralData) message;
				final InputStream pGPLiteralDataIs = pGPLiteralData.getInputStream();
				int ch;
				while ((ch = pGPLiteralDataIs.read()) >= 0)
				{
					decryptedFileOs.write(ch);
				}
			}
			else if (message instanceof PGPOnePassSignatureList)
			{
				encryptedFileIs.close();
				privKeyIn.close();

				if (decryptedFile.exists())
				{
					decryptedFile.delete();
				}
				throw new PGPException(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
						+ "Encrypted message contains a signed message - not literal data.");
			}
			else
			{
				encryptedFileIs.close();
				privKeyIn.close();
				if (decryptedFile.exists())
				{
					decryptedFile.delete();
				}
				throw new PGPException(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
						+ "Message is not a simple encrypted file - type unknown.");
			}

			if (pGPPublicKeyEncryptedData.isIntegrityProtected())
			{
				if (!pGPPublicKeyEncryptedData.verify())
				{
					encryptedFileIs.close();
					privKeyIn.close();
					if (decryptedFile.exists())
					{
						decryptedFile.delete();
					}
					throw new PGPException(
							Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN + "Message failed integrity check");
				}
			}
			encryptedFileIs.close();
			privKeyIn.close();
		}
		catch (final IOException | NoSuchProviderException | PGPException excpetion)
		{
			LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
					+ "Excpetion occured while Decrypting the File.", excpetion);
			try
			{
				if (null != encryptedFileIs)
				{
					encryptedFileIs.close();
				}
				if (null != privKeyIn)
				{
					privKeyIn.close();
				}

			}
			catch (final IOException iOException)
			{
				LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
						+ "Excpetion occured while Decrypting the File.", iOException);
			}
			throw new PGPException(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
					+ "Excpetion occured while Decrypting the File.");
		}
		finally
		{
			try
			{
				if (null != decryptedFileOs)
				{
					decryptedFileOs.close();
					if (decryptedFile.exists())
					{
						decryptedFile.delete();
					}
				}
			}
			catch (final IOException iOException)
			{
				LOGGER.error(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN
						+ "Excpetion occured while Decrypting the File.", iOException);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + DECRYPT_DATA + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return decryptedFile;
	}

	/**
	 * This method finds the PGPPrivateKey based on the privateKey and password phrase.
	 *
	 * @param pGPSecretKeyRingCollection
	 *           the gP secret key ring collection
	 * @param keyId
	 *           the key id
	 * @param passPhrase
	 *           the pass phrase
	 * @return PGPPrivateKey
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws PGPException
	 *            the pGP exception
	 * @throws NoSuchProviderException
	 *            the no such provider exception
	 */
	private static PGPPrivateKey findPrivateKey(final PGPSecretKeyRingCollection pGPSecretKeyRingCollection, final long keyId,
			final char[] passPhrase) throws IOException, PGPException, NoSuchProviderException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + FIND_PRIVATE_KEY + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		PGPPrivateKey pGPPrivateKey = null;
		LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + FIND_PRIVATE_KEY + Logging.HYPHEN + "Finding Private Key for Key ID["
				+ keyId + "]");

		final Iterator keyRing = pGPSecretKeyRingCollection.getKeyRings();
		while (keyRing.hasNext())
		{
			final PGPSecretKeyRing localPGPSecretKeyRing = (PGPSecretKeyRing) keyRing.next();
			final long secretKeyId = localPGPSecretKeyRing.getSecretKey().getKeyID();
			LOGGER.info(Logging.JNJ_ENCYPTION + Logging.HYPHEN + FIND_PRIVATE_KEY + Logging.HYPHEN
					+ "PGP Key Ring contains Secret Key with ID[" + secretKeyId + "]");
		}

		final PGPSecretKey pgpSecKey = pGPSecretKeyRingCollection.getSecretKey(keyId);

		if (pgpSecKey == null)
		{
			throw new PGPException(
					Logging.JNJ_ENCYPTION + Logging.HYPHEN + FIND_PRIVATE_KEY + Logging.HYPHEN + "Secret key for message not found.");
		}

		pGPPrivateKey = pgpSecKey.extractPrivateKey(passPhrase, BOUNCY_CASTLE_PROVIDER);

		if (pGPPrivateKey == null)
		{
			throw new PGPException(
					Logging.JNJ_ENCYPTION + Logging.HYPHEN + FIND_PRIVATE_KEY + Logging.HYPHEN + "Secret key for message not found.");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.JNJ_ENCYPTION + Logging.HYPHEN + FIND_PRIVATE_KEY + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return pGPPrivateKey;
	}

}
