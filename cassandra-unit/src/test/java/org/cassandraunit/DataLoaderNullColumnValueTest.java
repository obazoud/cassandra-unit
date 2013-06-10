package org.cassandraunit;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import me.prettyprint.cassandra.serializers.*;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.ddl.*;
import me.prettyprint.hector.api.exceptions.HInvalidRequestException;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.*;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.cassandraunit.model.StrategyModel;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.cassandraunit.utils.MockDataSetHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.cassandraunit.SampleDataSetChecker.assertDefaultValuesDataIsEmpty;
import static org.cassandraunit.SampleDataSetChecker.assertDefaultValuesSchemaExist;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * 
 * @author Jeremy Sevellec
 * 
 */
public class DataLoaderNullColumnValueTest {

	@BeforeClass
	public static void beforeClass() throws Exception {
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();
	}


	@Test
	public void shouldCreateKeyspaceAndColumnFamiliesWithDefaultValues() {
		String clusterName = "TestCluster40";
		String host = "localhost:9171";
		DataLoader dataLoader = new DataLoader(clusterName, host);

		dataLoader.load(MockDataSetHelper.getMockDataSetWithNullColumnValue());

		/* test */
		Cluster cluster = HFactory.getOrCreateCluster(clusterName, host);
        Keyspace keyspace = HFactory.createKeyspace("keyspaceWithNullColumnValue", cluster);
        ColumnQuery<String, String, String> query = HFactory.createColumnQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
        query.setColumnFamily("columnFamilyWithNullColumnValue");
        query.setKey("rowWithNullColumnValue").setName("columnWithNullValue");
        QueryResult<HColumn<String, String>> result = query.execute();
        assertThat(result.get().getValue(),is(""));
	}
}
