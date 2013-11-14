package org.richfaces.ui.chart;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;


@JavaScript("charttestutil")
@Dependency(sources = "org/richfaces/ui/chart/chart-testutil.js")
public interface ChartJs {

    String hello();
    int seriesLength(String id);
    int dataLength(String id, int seriesIndex);
    double pointX(String id, int seriesIndex, int pointIndex);
    double pointY(String id, int seriesIndex, int pointIndex);
    int pointXPos(String id, int seriesIndex, int pointIndex);
    int pointYPos(String id, int seriesIndex, int pointIndex);

}
