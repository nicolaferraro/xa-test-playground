/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package me.nicolaferraro.xa.playground;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author nicola
 * @since 22/05/2017
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SystemTest {

    @ArquillianResource
    private KubernetesClient client;

    private Random rnd = new Random();

    @Test
    public void test() throws Exception {

        for (int i = 0; i < 5; i++) {

            Optional<String> pod = getRandomTarget();
            if (pod.isPresent()) {
                System.out.println("POD: " + pod.get());
                System.out.println(client.pods().inNamespace("myproject").withName(pod.get()).delete());
            } else {
                System.out.println("NO POD");
            }
            Thread.sleep(30000);
        }


    }

    private Optional<String> getRandomTarget() {
        PodList podList = client.pods().withLabel("target-pod").list();
        List<Pod> pods = podList.getItems();
        if (pods.size() > 0) {
            int pos = rnd.nextInt(pods.size());
            return Optional.of(pods.get(pos).getMetadata().getName());
        }
        return Optional.empty();
    }

}
