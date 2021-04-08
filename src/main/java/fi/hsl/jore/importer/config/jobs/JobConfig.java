package fi.hsl.jore.importer.config.jobs;

import fi.hsl.jore.importer.feature.batch.link.LinkRowProcessor;
import fi.hsl.jore.importer.feature.batch.link.LinkRowReader;
import fi.hsl.jore.importer.feature.batch.link.LinkWriter;
import fi.hsl.jore.importer.feature.batch.link.dto.LinkRow;
import fi.hsl.jore.importer.feature.batch.node.NodeProcessor;
import fi.hsl.jore.importer.feature.batch.node.NodeReader;
import fi.hsl.jore.importer.feature.batch.node.NodeWriter;
import fi.hsl.jore.importer.feature.batch.point.LinkGeometryWriter;
import fi.hsl.jore.importer.feature.batch.point.LinkPointProcessor;
import fi.hsl.jore.importer.feature.batch.point.LinkPointReader;
import fi.hsl.jore.importer.feature.batch.point.PointReader;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkGeometry;
import fi.hsl.jore.importer.feature.batch.point.dto.LinkPoints;
import fi.hsl.jore.importer.feature.infrastructure.link.dto.PersistableLink;
import fi.hsl.jore.importer.feature.infrastructure.link.repository.ILinkRepository;
import fi.hsl.jore.importer.feature.infrastructure.node.dto.PersistableNode;
import fi.hsl.jore.importer.feature.infrastructure.node.repository.INodeRepository;
import fi.hsl.jore.importer.feature.jore.entity.JrNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration
public class JobConfig extends BatchConfig {

    @Bean
    public Job importJob(final Step importNodesStep,
                         final Step importLinksStep,
                         final Step importLinkPointsStep) {
        return jobs.get("importJoreJob")
                   .start(importNodesStep)
                   .next(importLinksStep)
                   .next(importLinkPointsStep)
                   .build();
    }

    @Bean
    public Step importNodesStep(final NodeReader nodeReader,
                                final INodeRepository nodeRepository) {
        return steps.get("importNodesStep")
                    .<JrNode, PersistableNode>chunk(NodeWriter.BLOCK_SIZE)
                    .reader(nodeReader.build())
                    .processor(new NodeProcessor())
                    .writer(new NodeWriter(nodeRepository))
                    .build();
    }

    @Bean
    public Step importLinksStep(final LinkRowReader linkReader,
                                final ILinkRepository linkRepository) {
        return steps.get("importLinksStep")
                    .<LinkRow, PersistableLink>chunk(LinkWriter.BLOCK_SIZE)
                    .reader(linkReader.build())
                    .processor(new LinkRowProcessor())
                    .writer(new LinkWriter(linkRepository))
                    .build();
    }

    @Bean
    public Step importLinkPointsStep(final PointReader pointReader,
                                     final ILinkRepository linkRepository) {
        return steps.get("importLinkPointsStep")
                    .<LinkPoints, LinkGeometry>chunk(LinkGeometryWriter.BLOCK_SIZE)
                    .reader(new LinkPointReader(pointReader.build()))
                    .processor(new LinkPointProcessor())
                    .writer(new LinkGeometryWriter(linkRepository))
                    .build();
    }
}
