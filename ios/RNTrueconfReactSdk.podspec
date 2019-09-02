
Pod::Spec.new do |s|
  s.name         = "RNTrueconfReactSdk"
  s.version      = "1.0.0"
  s.summary      = "RNTrueconfReactSdk"
  s.description  = <<-DESC
                  RNTrueconfReactSdk
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/kesha-antonov/RNTrueconfReactSdk.git", :tag => "master" }
  s.source_files  = "**/*.{h,m,swift}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end
