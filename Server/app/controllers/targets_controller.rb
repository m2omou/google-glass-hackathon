class TargetsController < ApplicationController


  # GET /targets
  # GET /targets.json
  def index
    @targets = Target.all
    @data = {:landmarks =>  JSON.parse(@targets.to_json(:include => {:answers => {:except => [:updated_at, :created_at]}},
                                                        :except => [:updated_at, :created_at])) }
    render json: @data
  end

  # GET /targets/1
  # GET /targets/1.json
  def show
    @target = Target.find(params[:id])

    render json: {:landmarks => @target.to_json(:include => :answers)}
  end

  # POST /targets
  # POST /targets.json
  def create
    @target = Target.new(target_params)

    if @target.save
      render json: @target, status: :created, location: @target
    else
      render json: @target.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /targets/1
  # PATCH/PUT /targets/1.json
  def update
    @target = Target.find(params[:id])

    if @target.update(params[:target])
      head :no_content
    else
      render json: @target.errors, status: :unprocessable_entity
    end
  end

  # DELETE /targets/1
  # DELETE /targets/1.json
  def destroy
    @target = Target.find(params[:id])
    @target.destroy

    head :no_content
  end

  private

  def target_params
    params.require(:target).permit(:longitude, :latitude, :picture, :fact, :title, :question)
  end
end
