INSERT INTO users (username, password, role) VALUES
                                                 ('organizer', '$2a$10$biblVBMQrhNjepJzTYtCxOi/dLBUV.O1fOKGfe7rplo44cndK0Jru', 'ROLE_ORGANIZER'),
                                                 ('attendee', '$2a$10$X1wLelwNX45YTRtT.3uvpuMgrvCOaV1wRSLIIPTWwk6OVQ0aSO9i.', 'ROLE_ATTENDEE'),
                                                 ('nonattendee', '$2a$10$oBsMPQgC85e1Lq8IdIRr2uMt2Tz1e8oHBqNLq8FAXPmYdN4vCLWWi', 'ROLE_ATTENDEE')
ON CONFLICT (username) DO NOTHING;

INSERT INTO events (id, name, description, date_time, organizer_id) VALUES
    (1, 'Spring Festival', 'A community spring gathering', '2025-06-01 18:00:00', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO event_users (event_id, user_id, role) VALUES
                                                      (1, 1, 'organizer'),
                                                      (1, 2, 'attendee')
ON CONFLICT (event_id, user_id) DO NOTHING;