/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.vts.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

/** Entity Class for saving Test Log Summary */
@Cache
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class TestSuiteResultEntity {

    public enum GROUP_TYPE {
        OTA, SIGNED, TOT
    }

    @Parent
    Key<TestSuiteFileEntity> testSuiteFileEntityKey;

    /** Test Suite start time field */
    @Id @Getter @Setter Long startTime;

    /** Test Suite end time field */
    @Getter @Setter Long endTime;

    /** Test Suite group type OTA, Signed, ToT field */
    @Index @Getter @Setter GROUP_TYPE groupType;

    /** Test Suite result path field */
    @Getter @Setter String resultPath;

    /** Test Suite host name field */
    @Getter @Setter String hostName;

    /** Test Suite plan field */
    @Index @Getter @Setter String suitePlan;

    /** Test Suite version field */
    @Getter @Setter String suiteVersion;

    /** Test Suite name field */
    @Getter @Setter String suiteName;

    /** Test Suite build number field */
    @Getter @Setter String suiteBuildNumber;

    /** Test Suite test finished module count field */
    @Getter @Setter int modulesDone;

    /** Test Suite total number of module field */
    @Getter @Setter int modulesTotal;

    /** Test Suite branch field */
    @Index @Getter @Setter String branch;

    /** Test Suite build target field */
    @Index @Getter @Setter String target;

    /** Test Suite build ID field */
    @Index @Getter @Setter String buildId;

    /** Test Suite system fingerprint field */
    @Getter @Setter String buildSystemFingerprint;

    /** Test Suite vendor fingerprint field */
    @Getter @Setter String buildVendorFingerprint;

    /** Test Suite test count for success field */
    @Index @Getter @Setter int passedTestCaseCount;

    /** Test Suite test count for failure field */
    @Index @Getter @Setter int failedTestCaseCount;

    /** Test Suite ratio of success to find candidate build */
    @Index @Getter @Setter int passedTestCaseRatio;

    /** When this record was created or updated */
    @Index @Getter Date updated;

    /** Construction function for TestSuiteResultEntity Class */
    public TestSuiteResultEntity(
            Key<TestSuiteFileEntity> testSuiteFileEntityKey,
            Long startTime,
            Long endTime,
            String resultPath,
            String hostName,
            String suitePlan,
            String suiteVersion,
            String suiteName,
            String suiteBuildNumber,
            int modulesDone,
            int modulesTotal,
            String branch,
            String target,
            String buildId,
            String buildSystemFingerprint,
            String buildVendorFingerprint,
            int passedTestCaseCount,
            int failedTestCaseCount) {
        this.testSuiteFileEntityKey = testSuiteFileEntityKey;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultPath = resultPath;
        this.hostName = hostName;
        this.suitePlan = suitePlan;
        this.suiteVersion = suiteVersion;
        this.suiteName = suiteName;
        this.suiteBuildNumber = suiteBuildNumber;
        this.modulesDone = modulesDone;
        this.modulesTotal = modulesTotal;
        this.branch = branch;
        this.target = target;
        this.buildId = buildId;
        this.buildSystemFingerprint = buildSystemFingerprint;
        this.buildVendorFingerprint = buildVendorFingerprint;
        this.passedTestCaseCount = passedTestCaseCount;
        this.failedTestCaseCount = failedTestCaseCount;

        int totalTestCaseCount = passedTestCaseCount + failedTestCaseCount;
        if ( totalTestCaseCount <= 0 ) {
            this.passedTestCaseRatio = 0;
        } else {
            this.passedTestCaseRatio = passedTestCaseCount / totalTestCaseCount * 100;
        }
    }

    /** Saving function for the instance of this class */
    public void save() {
        this.updated = new Date();
        ofy().save().entity(this).now();
    }

    public List<? extends TestSuiteResultEntity> getTestSuitePlans() {
        return ofy().load().type(this.getClass()).project("suitePlan").distinct(true).list();
    }
}
