INSERT INTO users (username, password, role) VALUES
                                                 ('organizer', '$2a$10$biblVBMQrhNjepJzTYtCxOi/dLBUV.O1fOKGfe7rplo44cndK0Jru', 'ROLE_ORGANIZER'),
                                                 ('attendee', '$2a$10$X1wLelwNX45YTRtT.3uvpuMgrvCOaV1wRSLIIPTWwk6OVQ0aSO9i.', 'ROLE_ATTENDEE')
    ON CONFLICT (username) DO NOTHING;